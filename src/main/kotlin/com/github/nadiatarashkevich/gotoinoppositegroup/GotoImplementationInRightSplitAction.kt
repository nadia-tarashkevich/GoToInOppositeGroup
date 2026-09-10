package com.github.nadiatarashkevich.gotoinoppositegroup

import com.intellij.openapi.fileEditor.ex.FileEditorManagerEx
import com.intellij.openapi.fileEditor.impl.EditorWindow
import com.intellij.openapi.vfs.VirtualFile

/**
 * Shows the implementation in a group to the right, reusing the group next to the current one when
 * there is one and splitting off a new group otherwise - the same rule the platform's own
 * "Open in Right Split" action follows.
 */
class GotoImplementationInRightSplitAction : GotoImplementationInSplitAction() {

    override fun findTargetWindow(
        fem: FileEditorManagerEx,
        currentWindow: EditorWindow,
        targetFile: VirtualFile,
    ): EditorWindow? {
        // requestFocus is explicit to avoid the synthetic `$default` bridge.
        return fem.splitters.openInRightSplit(targetFile, true)
    }
}
