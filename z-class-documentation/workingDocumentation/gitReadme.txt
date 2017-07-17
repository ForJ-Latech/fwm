Getting Git set up for Eclipse:
	1. Right-click -> Import
	2. Git -> Projects from Git -> Next >
	3. Clone URI
	4. Enter in Location: URI: http://138.47.200.245/jehlmann/fwm.git
	5. Click Next
	6. Click Next
	7. Change Directory to wherever you want (i.e. Eclipse workspace)
	8. Change Initial branch to "staging"
	9. Click Next
	10. Make sure Import existing Eclipse Projects is ticked under "Wizard for project import"
	11. Select "fwm"
	12. Click Finish

Pushing to Git:
	1. Right click project in Package Explorer
	2. Go under "Team"
	3. Click "Commit"
	4. A window will pop up, with a window called "Unstaged Changes", "Staged Changes", and "Commit Message".
	5. Unstaged Changes are changes that will -not- go into the current commit. Move over the file to "Staged Changes" to have it change it during this commit.
	6. After you are done adding all your unstaged changes to staged changes that you want, add a commit message.
	7. Click "Commit and Push". (Note: If you click "commit", it will commit only for your local repo, so NO ONE CAN SEE YOUR CHANGES EXCEPT YOU, until your push!)

Pulling from Git:
	1. Right click project in Package Explorer
	2. Go under "Team"
	3. Click "Pull" (Note: there are two "pull's", I am not sure if there is a difference, but there should not be).
	4. Your working branch should be updated to the latest commit.

Viewing Commits on Gitlab site:
	1. Navigate to http://138.47.200.245/jehlmann/fwm/
	2. Click "Activity"
	3. A list of commits will be shown. Click on the commit id to see further information. (Should be on the left of the commit message)
	4. Click on "_ changed file" to see the list of changed files.