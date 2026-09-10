<!-- Keep a Changelog guide -> https://keepachangelog.com -->

# GoToInOppositeGroup Changelog

## [Unreleased]
### Added
- "Goto Implementation in Right Split" action, which always navigates into a group to the right instead of alternating
  between groups: it reuses the group next to the current one, or splits off a new one when the current group is the
  rightmost. Both actions have their own keymap entry, so they can be bound separately
- Ctrl-mouse highlighting: with a mouse shortcut such as `Cmd+Opt+Click` bound to either action, hovering over a symbol
  underlines it, switches to the hand cursor and shows the usual info hint

### Fixed
- The opposite group no longer gets an extra tab with the file the action was invoked from
- `NoSuchMethodError` on IntelliJ IDEA 2026.2: the action no longer opens the editor through the internal
  `FileEditorOpenOptions`, whose constructor signature changed in the 2026.2 platform. It now targets the opposite
  split with `EditorWindow.setAsCurrentWindow()` + `OpenFileDescriptor`, which is stable public API

## [1.0.0] - 2026-09-09
### Added
- "Goto Implementation in Opposite Group" action that navigates to a symbol's implementation in the opposite editor split
- Automatically creates a split if one does not exist
- Available from Navigate menu, editor context menu, and Search Everywhere
