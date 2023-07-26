# CST2335 - Mobile Application Development Final Project

| Student Name    | Email                      | Project Name              |
|-----------------|----------------------------|---------------------------|
| Iseul Park      | park0586@algonquinlive.com | Aviation Stack Flight Tracker  |
| Ahmed Almutawakel | almu0065@algonquinlive.com | Trivia Question Database |
| Hakan Okay      | okay0003@algonquinlive.com | Bear Image Generator     |
| Dowon Kang      | kang0095@algonquinlive.com | Currency Converter       |

Code-freeze: Aug 1, 2023

## Git Usage

This project utilizes Git for version control, allowing for collaborative development and easy
management of code changes. Here is a more detailed explanation of Git usage:

### Cloning the Repository

1. Open a terminal or command prompt.
2. Navigate to the directory where you want to clone the repository.
3. Run the following command to clone the repository:

   ```bash
   git clone <repository-url>
   ```

4. Once the repository is cloned, navigate into the project directory:

   ```bash
   cd cst2335
   ```

### Creating a New Branch

Before starting work on a new feature or bug fix, it's recommended to create a new branch. This
allows you to isolate your changes and makes it easier to merge them later.

1. Ensure you are on the `main` branch:

   ```bash
   git checkout main
   ```

2. Create a new branch with a descriptive name for your feature or bug fix:

   ```bash
   git checkout -b <branch-name>
   ```

   Replace `<branch-name>` with the desired name for your branch.

3. You are now on the new branch and ready to start working.

### Making Changes and Committing

1. Open the project in your preferred development environment and make the necessary changes to your
   assigned Java package.

2. Once you have made some changes, save your files and return to the terminal.

3. Check the status of your changes:

   ```bash
   git status
   ```

   This will show you the modified files and any untracked files.

4. Add the changes to the staging area:

   ```bash
   git add <file1> <file2> ...
   ```

   Replace `<file1> <file2> ...` with the names of the files you want to stage. Alternatively, you
   can use `git add .` to stage all changes.

5. Commit your changes with a meaningful commit message:

   ```bash
   git commit -m "Your commit message here"
   ```

   Provide a clear and concise message describing the changes you made.

6. Repeat steps 3-5 as you continue making changes.

### Pushing Changes and Creating Pull Requests

1. When you are ready to share your changes with the team, push your branch to the remote
   repository:

   ```bash
   git push origin <branch-name>
   ```

   Replace `<branch-name>` with the name of your branch.

2. Go to the repository's page on the Git hosting platform (e.g., GitHub, GitLab) and locate your
   pushed branch.

3. Create a pull request (or merge request) to initiate the process of merging your changes into
   the `main` branch. Provide a descriptive title and description for the pull request.

4. Collaborate with your team members and address any feedback or comments on the pull request.

5. Once the pull request is approved, it can be merged into the `main` branch by a team member with
   the necessary permissions.

### Updating Your Local Repository

To ensure your local repository is up to date with the latest changes from the remote repository:

1. Switch to the `main` branch:

   ```
   git checkout main
   ```

2. Pull the latest changes:

   ```
   git pull origin main
   ```

3. Switch back to your working branch:

   ```
   git checkout <branch-name>
   ```

   Replace `<branch-name>` with the name of your branch.

4. If there were changes in the `main` branch, you can merge them into your branch:

   ```
   git merge main
   ```

   Resolve any conflicts that may arise during the merge.

### Additional Git Commands

Here are a few additional Git commands that may come in handy:

- `git branch`: Lists all branches in the repository.
- `git branch -d <branch-name>`: Deletes a branch locally.
- `git pull`: Fetches and merges changes from the remote repository.
- `git log`: Shows the commit history.
- `git diff`: Shows the differences between files.

For more detailed information and advanced usage, refer to the official Git documentation or other
Git resources.

Remember to adhere to the team's Git workflow and follow any specific guidelines provided by your
project supervisor or instructor.

## Gradle and Dependencies

Gradle is used as the build system for the project. To add dependencies to your specific Java
package, follow the steps below:

1. Open the **build.gradle** file located in the respective package directory.

- **For the main app module:** app/build.gradle
- **For unit tests:** app/src/test/build.gradle
- **For Android instrumented tests:** app/src/androidTest/build.gradle

2. In the dependencies block, add the desired dependency using the following syntax:

    ```gradle
    dependencies {
        implementation 'group:name:version'
    }
    ```

3. Replace group, name, and version with the actual values for the dependency you want to include.
   For example:

    ```gradle
    dependencies {
        implementation 'com.example:library:1.0.0'
    }
    ```

4. Sync the project with Gradle to download and apply the new dependency.

5. Repeat these steps for each Java package in which you want to add dependencies.
