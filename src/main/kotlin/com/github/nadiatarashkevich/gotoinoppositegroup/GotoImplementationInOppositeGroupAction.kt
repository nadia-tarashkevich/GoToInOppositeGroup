package com.github.nadiatarashkevich.gotoinoppositegroup

import com.intellij.codeInsight.navigation.actions.GotoDeclarationAction
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.editor.ScrollType
import com.intellij.openapi.fileEditor.TextEditor
import com.intellij.openapi.fileEditor.ex.FileEditorManagerEx
import com.intellij.openapi.fileEditor.impl.FileEditorOpenOptions
import javax.swing.SwingConstants

class GotoImplementationInOppositeGroupAction : AnAction() {

    override fun getActionUpdateThread(): ActionUpdateThread = ActionUpdateThread.BGT

    override fun actionPerformed(e: AnActionEvent) {
        val project = e.project ?: return
        val editor = e.getData(CommonDataKeys.EDITOR) ?: return

        val offset = editor.caretModel.offset
        val targetElements = GotoDeclarationAction.findAllTargetElements(project, editor, offset)
        if (targetElements.isEmpty()) return

        val target = targetElements[0]
        val targetFile = target.containingFile?.virtualFile ?: return
        val targetOffset = target.textOffset

        val fem = FileEditorManagerEx.getInstanceEx(project)
        val currentWindow = fem.currentWindow ?: return

        val oppositeWindow = fem.getNextWindow(currentWindow).let { next ->
            if (next != null && next != currentWindow) {
                next
            } else {
                fem.createSplitter(SwingConstants.VERTICAL, currentWindow)
                fem.getNextWindow(currentWindow) ?: return
            }
        }

        val composite = fem.openFile(
            file = targetFile,
            window = oppositeWindow,
            options = FileEditorOpenOptions(requestFocus = true),
        )

        val textEditor = composite.allEditors.filterIsInstance<TextEditor>().firstOrNull()
        textEditor?.editor?.let {
            it.caretModel.moveToOffset(targetOffset)
            it.scrollingModel.scrollToCaret(ScrollType.CENTER)
        }
    }

    override fun update(e: AnActionEvent) {
        val project = e.project
        val editor = e.getData(CommonDataKeys.EDITOR)
        e.presentation.isEnabledAndVisible = project != null && editor != null
    }
}
