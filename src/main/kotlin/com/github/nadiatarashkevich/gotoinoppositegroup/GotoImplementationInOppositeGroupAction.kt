package com.github.nadiatarashkevich.gotoinoppositegroup

import com.intellij.openapi.fileEditor.ex.FileEditorManagerEx
import com.intellij.openapi.fileEditor.impl.EditorWindow
import com.intellij.openapi.vfs.VirtualFile
import javax.swing.SwingConstants

/**
 * Shows the implementation in the opposite editor group, cycling through the groups in visual order
 * the way "Move Tab to Opposite Group" does.
 */
class GotoImplementationInOppositeGroupAction : GotoImplementationInSplitAction() {

    override fun findTargetWindow(
        fem: FileEditorManagerEx,
        currentWindow: EditorWindow,
        targetFile: VirtualFile,
    ): EditorWindow? {
        // getNextWindow() wraps around at the last group and returns the current window when that is
        // the only group, which is when the split still has to be created.
        return fem.getNextWindow(currentWindow)?.takeIf { it != currentWindow }
            // All arguments explicit: relying on a default calls the synthetic `split$default`
            // bridge, whose signature shifts whenever the platform adds another defaulted parameter.
            ?: currentWindow.split(
                SwingConstants.VERTICAL,
                false,
                targetFile,
                true,
                true,
            )
    }
}
