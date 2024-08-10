# Git Conventions

## Ignored files

These files should be listed in `.gitignore`:

- All IDE/Editor-specific files
- Build artifacts

To prevent unexpected ignoring files in subdirectories:

- Each record in `.gitignore` should start with `/`.
- Wildcards like `**/*.iml` are prohibited. If you need to ignore `*.iml` files in a subdirectory, create a
  separate `.gitignore` there that contains `/*.iml`.

To ignore local files without listing them in `.gitignore`s tracked by Git,
either ignore the whole directory with `.gitignore` that contains `**`
or list them in `.git/info/exclude`.

## Commits

Each commit should go with its author's name and a message that briefly describes what was done.
So make sure that `user.name` Git setting is set and that you commit with `-m`/`--message`.

To make Git history more understandable, it's recommended to make commits small and logically atomic.

## Merging

Only fast-forward merging is allowed, merge commits are prohibited.
If fast-forward merge is not currently possible, use `git rebase`.

## Force push

Force pushes to `main` are prohibited.

To force push to another branch, it's recommended to use `--force-with-lease` option.
