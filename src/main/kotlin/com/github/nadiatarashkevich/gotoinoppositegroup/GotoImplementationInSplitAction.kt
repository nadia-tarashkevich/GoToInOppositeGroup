package com.github.nadiatarashkevich.gotoinoppositegroup

import com.intellij.codeInsight.navigation.CtrlMouseAction
import com.intellij.codeInsight.navigation.CtrlMouseData
import com.intellij.codeInsight.navigation.CtrlMouseHandler
import com.intellij.codeInsight.navigation.actions.GotoDeclarationAction
import com.intellij.openapi.actionSystem.ActionUpdateThread
import com.intellij.openapi.actionSystem.AnAction
import com.intellij.openapi.actionSystem.AnActionEvent
import com.intellij.openapi.actionSystem.CommonDataKeys
import com.intellij.openapi.editor.Editor
import com.intellij.openapi.fileEditor.FileEditorManager
import com.intellij.openapi.fileEditor.OpenFileDescriptor
import com.intellij.openapi.fileEditor.ex.FileEditorManagerEx
import com.intellij.openapi.fileEditor.impl.EditorWindow
import com.intellij.openapi.util.TextRange
import com.intellij.openapi.vfs.VirtualFile
import com.intellij.psi.PsiFile

/**
 * Resolves the implementation of the symbol under the caret and shows it in another editor group.
 * Subclasses only pick which group that is.
 */
abstract class GotoImplementationInSplitAction : AnAction(), CtrlMouseAction {

    /**
     * Returns the editor window the implementation should be shown in, creating a split if there is
     * no suitable one yet, or null if there is nowhere to navigate to.
     *
     * [targetFile] is passed in for the platform's splitting APIs: given no file, those clone the
     * current window's selected tab into the new group, leaving the source file next to the
     * implementation.
     */
    protected abstract fun findTargetWindow(
        fem: FileEditorManagerEx,
        currentWindow: EditorWindow,
        targetFile: VirtualFile,
    ): EditorWindow?

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
        val targetWindow = findTargetWindow(fem, currentWindow, targetFile) ?: return

        // Navigation goes into whichever window is current, so make the target one current first.
        // FileEditorManagerEx.openFile() would be more direct, but it takes an internal
        // FileEditorOpenOptions whose constructor signature changes between platform releases (a
        // build against 2025.2 dies with NoSuchMethodError on 2026.2).
        targetWindow.setAsCurrentWindow(true)

        val descriptor = OpenFileDescriptor(project, targetFile, targetOffset)
            // Do not let the platform reuse another split that already shows the file - the point of
            // the action is to land in the group picked above.
            .setUseCurrentWindow(true)

        FileEditorManager.getInstance(project).openTextEditor(descriptor, true)
    }

    /**
     * Drives the ctrl-mouse highlighting: the underline and the hand cursor shown while the mouse
     * shortcut's modifiers are held down. The platform only does that for the action bound to those
     * modifiers when it implements [CtrlMouseAction].
     */
    override fun getCtrlMouseData(editor: Editor, file: PsiFile, offset: Int): CtrlMouseData? {
        // Non-physical elements (completion previews and the like) have no range to highlight.
        val elementAtPointer = file.findElementAt(offset)?.takeIf { it.isPhysical } ?: return null
        val pointerRange = elementAtPointer.textRange ?: return null

        // Resolved exactly the way actionPerformed() does - including taking the first target when
        // there are several - so the highlighting and the hint describe what the click will open.
        val target = GotoDeclarationAction
            .findAllTargetElements(file.project, editor, offset)
            .firstOrNull() ?: return null

        // The platform builds this same value in psiCtrlMouseData(), but that one is Kotlin
        // `internal`, so it is off limits here.
        val highlighted = TextRange(
            maxOf(elementAtPointer.textOffset, pointerRange.startOffset),
            pointerRange.endOffset,
        )
        return CtrlMouseData(
            listOf(highlighted),
            true,
            CtrlMouseHandler.getInfo(target, elementAtPointer),
            null,
        )
    }

    override fun update(e: AnActionEvent) {
        val project = e.project
        val editor = e.getData(CommonDataKeys.EDITOR)
        e.presentation.isEnabledAndVisible = project != null && editor != null
    }
}
