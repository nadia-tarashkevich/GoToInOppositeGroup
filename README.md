# Goto Implementation in Opposite Group

![Build](https://github.com/nadia-tarashkevich/GoToInOppositeGroup/workflows/Build/badge.svg)

An IntelliJ Platform plugin that navigates to a symbol's implementation directly in another editor split, without leaving your current position. It adds two actions:

- **Goto Implementation in Opposite Group** — opens the implementation in the opposite group, alternating back and forth as you use it.
- **Goto Implementation in Right Split** — always opens the implementation in a group to the right: the group next to the current one when there is one, otherwise a new group split off to the right.

Either action creates the split when the editor is not split yet. Both have their own <kbd>Keymap</kbd> entry, so you can bind a separate shortcut to each (or only to the one you use).

## Screenshot

![Goto Implementation in Opposite Group](img.png)

## How to Use

1. Place the caret on a symbol whose implementation you want to inspect.
2. Run either action in one of the following ways (substitute `Goto Implementation in Right Split` for the right-split variant):
   - **Menu**: <kbd>Navigate</kbd> → <kbd>Goto Implementation in Opposite Group</kbd>
   - **Editor context menu**: Right-click → <kbd>Go To</kbd> → <kbd>Goto Implementation in Opposite Group</kbd>
   - **Search Everywhere**: Press <kbd>Shift</kbd> twice, then type `Goto Implementation in Opposite Group`
   - **Custom shortcut**: Assign one via <kbd>Settings</kbd> → <kbd>Keymap</kbd> → search for `Goto Implementation`
3. The implementation opens in the other editor split with the caret at the target definition.

## Installation

- Using the IDE built-in plugin system:

  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>Marketplace</kbd> > <kbd>Search for "GoToInOppositeGroup"</kbd> >
  <kbd>Install</kbd>

- Manually:

  Download the [latest release](https://github.com/nadia-tarashkevich/GoToInOppositeGroup/releases/latest) and install it manually using
  <kbd>Settings/Preferences</kbd> > <kbd>Plugins</kbd> > <kbd>⚙️</kbd> > <kbd>Install plugin from disk...</kbd>
