# Jenkins Pipeline - Generate Release Notes

The provided script is a Jenkins pipeline written in Groovy. It serves the purpose of generating release notes for multiple repositories based on selected versions. Let's break down the Jenkinsfile step by step and provide an explanation for each part.

## Purpose

The purpose of this pipeline is to streamline the release process and reduce the manual effort required to create release notes for a set of repositories. By providing a user-friendly interface to select repositories, credentials, branch names, and versions, this script simplifies the release note generation process.

## Overview

This Jenkins pipeline script automates the process of generating release notes for multiple repositories based on selected versions. The pipeline is designed to work in a Jenkins environment and utilizes Groovy as its scripting language.

## Prerequisites

To use this Jenkins pipeline, you need the following:

1. Jenkins: The pipeline should be run within a Jenkins environment.
2. Shared Library: The pipeline uses a shared library named 'pipeline-libs'. Make sure the library is available and accessible in your Jenkins setup.

## Usage

1. Create a new Jenkins job or configure an existing one to use this Jenkinsfile as its pipeline script.
2. Set up the required parameters for the job:
   - **REPOSITORY**: A multi-select choice parameter to select one or more repositories from a predefined list.
   - **CREDENTIALS_ID**: A string parameter that provides the user credentials to push without Pull Request constraints to repositories.
   - **BRANCH_NAME**: A string parameter with a default value 'develop' that specifies the branch name.
   - **VERSION**: A string parameter that specifies the version for which the changelog should be generated.

3. Run the Jenkins job with the specified parameters.

## How it Works

1. The pipeline starts by taking user inputs for repository selection, credentials, branch name, and version.
2. It clones the selected repositories and retrieves the changelog information for each one.
3. The pipeline then constructs the release notes using a predefined markdown template and inserts the changelog for each repository at the appropriate location.
4. The script also collects Jira IDs mentioned in the changelog and includes them in a separate section of the release notes.
5. Finally, the pipeline updates the release notes with actual version information and displays the generated release notes.


## Jenkins Pipeline Stages

1. **#!/usr/bin/env groovy**: This line specifies the shebang to indicate that the script is written in Groovy.

2. **@Library('pipeline-libs') _**: This directive indicates that the pipeline uses a shared library named 'pipeline-libs'.

3. **properties**: This block defines the Jenkins job properties. It allows users to input parameters before starting the pipeline execution.

   - **parameters**: This block defines the list of parameters that users need to input. The parameters are as follows:
      - **REPOSITORY**: A multi-select choice parameter that allows users to select one or more repositories from a list.
      - **CREDENTIALS_ID**: A string parameter that provides the user credentials to push without Pull Request constraints to repositories.
      - **BRANCH_NAME**: A string parameter with a default value 'develop' that specifies the branch name.
      - **VERSION**: A string parameter that specifies the version for which the changelog should be generated.

4. **def prefix**: This variable stores the initial part of the release notes template. It contains a markdown template for the release notes with some placeholders for the repositories and version.

5. **def jiraIdsPrefix**: This variable stores the initial part of the Jira IDs section in the release notes.

6. **scanDir**: This variable stores the name of the directory that will be created to store changelog data.

7. **node('be-slave-jdk17')**: This block specifies the Jenkins agent/node where the pipeline will run. In this case, it is 'be-slave-jdk17'.

8. **stage('Provision container')**: This block represents a Jenkins pipeline stage named 'Provision container'.

9. **container('be-slave-jdk17')**: This block runs the subsequent stages inside a Docker container with the label 'be-slave-jdk17'.

10. **stage('Prepare Workspace')**: This block represents a Jenkins pipeline stage named 'Prepare Workspace'. It creates a directory named 'changelog-sources'.

11. **stage('Generate release notes')**: This block represents a Jenkins pipeline stage named 'Generate release notes'. It performs the actual generation of release notes.

12. **def builder**: This variable is a StringBuilder object used to construct the final release notes.

13. **def jiraIds**: This variable stores a HashSet to collect unique Jira IDs found in the changelog.

14. **def versionsPropertiesFile**: This variable stores the path to the 'versions.properties' file.

15. **def projects**: This variable stores the list of repositories selected by the user as input.

16. **sh "mkdir versions && touch ${versionsPropertiesFile}"**: This shell command creates a 'versions' directory and an empty 'versions.properties' file.

17. **echo "No repositories selected. Using default repositories."**: This echo statement is displayed if no repositories are selected.

18. **builder.append(prefix)**: This line appends the initial part of the release notes template to the 'builder' variable.

19. **dir('projectData')**: This block changes the current working directory to 'projectData' within the Docker container.

20. **withMaven(...)**: This block specifies Maven configuration to be used within the container.

21. **projects.each {...}**: This loop iterates over the selected repositories.

22. **String changelog = getChangelog("${it}", versionsPropertiesFile)**: This line calls the 'getChangelog' function to retrieve the changelog for each repository.

23. **jiraIds.addAll(getJiraIds(changelog))**: This line extracts Jira IDs from the changelog and adds them to the 'jiraIds' HashSet.

24. **builder.append(jiraIdsPrefix)**: This line appends the initial part of the Jira IDs section to the 'builder' variable.

25. **jiraIds.each {...}**: This loop iterates over the collected Jira IDs and appends each Jira ID to the 'builder' variable.

26. **changelog = updateChangelogWithVersions(builder.toString(), versionsPropertiesFile)**: This line updates the 'builder' variable, replacing placeholders for version information with actual versions from 'versions.properties'.

27. **echo changelog**: This echo statement displays the final generated release notes.

28. **Set<String> getJiraIds(changelog)**: This function takes the changelog content as input and returns a HashSet of unique Jira IDs found in the changelog.

29. **String getChangelog(project, versionsPropertiesFile)**: This function takes the repository name and 'versions.properties' file as inputs, performs changelog concatenation for the specified repository, and returns the corresponding changelog section.

30. **def updateChangelogWithVersions(changelog, versionsPropertiesFileString)**: This function updates the changelog content by replacing placeholders for versions with actual version information read from 'versions.properties'.

31. **def appendProjectVersionsToFile(versionsPropertiesFile)**: This function appends the versions of each project to the 'versions.properties' file.

32. **def gitCheckout(url, credentials, branchName)**: This function checks out the specified repository using Git.

This is a brief overview of the Jenkinsfile, explaining its purpose and each stage's functionality. The pipeline script automates the generation of release notes, helping to streamline the release process for multiple repositories.


## Note

- The pipeline uses a 'versions.properties' file to keep track of project versions. Make sure to set up and maintain this file for accurate release note generation.
- Sample Changelog file is also shared under the folder.
