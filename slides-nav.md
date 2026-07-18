# Code Dump: slides-nav

Instruction for AI: When making edits, please provide your changes as:
NOTE: there should not any text/character/symbol/anything between the updated code. The delimiter must have exactly 7 characters
SEARCH/REPLACE block:
## relative/path/to/file.ext
<<<<<<< SEARCH
old code
=======
new code
>>>>>>> REPLACE

To replace all code in a file (CRITICAL: MUST use this for modularization, total rewrites, or when modifying major portions of files like style.css, HTML, or large code files to avoid tedious and error-prone search/replace blocks):
## relative/path/to/file.ext
<<<<<<< REPLACE_ALL
new file content
>>>>>>> REPLACE_ALL

To create a new file, specify the new path under the ## header and leave the SEARCH block empty:
## relative/path/to/new_file.ext
<<<<<<< SEARCH
=======
new file content
>>>>>>> REPLACE

To move or rename a file, use the '->' operator in the ## header:
## relative/path/to/old.ext -> relative/path/to/new.ext
<<<<<<< SEARCH
=======
>>>>>>> REPLACE

To delete a file or folder (Delete workaround):
- To delete a file, move the file that needs to be deleted to the 'recycle-bin' folder:
## relative/path/to/file.ext -> recycle-bin/file.ext
<<<<<<< SEARCH
=======
>>>>>>> REPLACE

- To delete a folder, rename the folder to be deleted with a name like 'recycle-bin-folder-name':
## relative/path/to/folder-name -> relative/path/to/recycle-bin-folder-name
<<<<<<< SEARCH
=======
>>>>>>> REPLACE

## slides-nav/agents/instructions.md

### Instructions (Always edit this file when making changes to make the instructions accurate)

This project is about [insert project description]

Anti Emoji, no emoji allowed

Anti Generic UI styles, use simple and modern UI, not generic AI/Colorful UI styles

### List of Changes (Always add new changes here, do not delete any previous changes)

What changes have been made?


## slides-nav/slides-nav.md (1 lines)

```markdown

```

## slides-nav/agents/instructions.md (12 lines)

```markdown
### Instructions (Always edit this file when making changes to make the instructions accurate)

This project is about [insert project description]

Anti Emoji, no emoji allowed

Anti Generic UI styles, use simple and modern UI, not generic AI/Colorful UI styles

### List of Changes (Always add new changes here, do not delete any previous changes)

What changes have been made?

```

## slides-nav/agents/must_include.md (1 lines)

```markdown

```

