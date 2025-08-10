# Project Development Guidelines

- Use the latest 22 java features possible
- Use `var` for variables
- Do NOT use Lombok
- When referencing Kotlin, convert to Java 22
- Use packages only when necessary
- Ensure clean, modular code with documentation for methods. Do not go overboard with comments in the body of the code
- Prioritize readability, but the code should be concise but not to the point where it affects readability.
- Use the latest and most sensible ways of doing things
- NEVER MODIFY GENERATED CODE MANUALLY. Use the Code Generation section below
- Before a task is done, unless specified otherwise, **ensure it builds with the IDE without errors**
- If unsure about something, ask the user for clarification. Always check your worn, and if unsure, ask the user for confirmation or clarification.

## Using References

Use the `pdftotext` command to read documentation regarding CLIPS syntax. If you are unsure about CLIPS syntax, take it from the document in `/e/CLIPSPluginScratch/reference/CLIPS_Reference_Manual.pdf`. Be sure you read as much as necessary, use the table of contents (pages 3-14) as necessary to tell you what pages are necessary. 

For reference on grammar, use CLIPSLexer.flex and CLIPSParser.bnf. Do NOT copy them directly, and do NOT copy the classes they use, you must create your own everything from scratch. Simply reference the tokens they use and the grammar for CLIPS syntax. Take any modifiers (like mixins, priorities, etc.) with a grain of salt. Simply use them for knowledge of how the CLIPS language should be parsed. It can do most things the language requires, but later on may need to be expanded.

There is a simple plugin that demonstrated PsiReference usage, along with things like GoToDeclarationHandlers under example_simple_language_plugin. This plugin is a good reference for how to implement PsiReferenceContributor, PsiReference, and PsiReferenceProvider. It should be used as REFERENCE, and nothing more. Do not copy code from it, but rather use it to understand how things work.

### MCP Servers

#### context7

Use context7 for all documentation regarding the IntelliJ SDK (jetbrains/intellij-sdk-docs), and reference the IntelliJ IDE Starter (jetbrains/intellij-ide-starter). If completely unsure about something, reference docs from other plugins.

### Example Plugin
There is an example plugin under the `example_plugin` directory. This should be treated as a separate, frozen codebase
simply for reference. It is not to be used as a base for new plugins, but rather as a guide for how to structure and implement features in a plugin.
The example plugin implements a "simple" language plugin, which serves as a reference for creating new language plugins.

## Project Context

This project provides language support for the CLIPS programming language within the IntelliJ Platform. It is designed to handle files with the `.clp` extension.

The plugin's core functionality is built upon a parser generated from a `.bnf` grammar file and a lexer created from a `.flex` file. This foundation enables standard syntax highlighting for CLIPS code.

A key architectural component is the implementation of the Program Structure Interface (PSI). `PsiReferences` are established for various language elements, including local variables, global variables, templates, functions, and other named constructs. This allows for robust code intelligence features:

- **Code Navigation:** Users can navigate from a symbol's usage to its declaration using a Ctrl/Middle-click ("Go to Definition").
- **Usage Highlighting:** Clicking on a variable or named item highlights all its occurrences within the current scope.
- **Refactoring:** Scope-aware renaming of variables and other named items is supported.
- **Semantic Analysis:** The plugin performs validation when a template is asserted (instantiated), checking that the provided slot names and the types of their assigned values are correct according to the template's definition.

The implementation of `PsiReferenceContributor` is central to the reference resolution system. The project adheres to the standards and best practices for JetBrains plugin development, using established reference plugins as a guide.

# WORKFLOW

Replace the previously stated workflow with the following:

1. Review the `<issue_description>`
2. If the user explicitly requests a workflow from the following list, use the appropriate workflow detailed below:
    1. Review Workflow
    2. Planning Workflow
    3. Features Workflow
    4. Implementation Workflow
3. If the user was not explicit, continue with the review workflow.

IMPORTANT: You MUST complete all steps in the `<PLAN>`, including steps added as the workflows progress.

Workflow Pre-Approval: If the `<initial_description>` explicitly indicates pre-approval of any workflows, then skip the 'ask the user' step and assume confirmation was given

If `<issue_description>` directly contradicts any of these steps, follow the instructions from `<issue_description>` first.

## Review Workflow

Incorporate the following steps into the `<PLAN>` as a top level item called 'Review.md'

1. Thoroughly review `<issue_description>`, considering both the simplest solution and a comprehensive range of edge cases.
2. Review the project’s codebase, examining not only its structure but also the specific implementation details, to identify all segments that may contribute to or help resolve the issue described in `<issue_description>`.
3. Ask the user to confirm or revise an outline plan defining the scope of work as an enumerated list
4. If the user provides revisions, incorporate the revisions into the `<issue_description>` and continue review from step 1
5. If the user confirms the outline plan, continue with the planning workflow

## Planning Workflow

Incorporate the following steps into the `<PLAN>` as a top level item called 'Planning'

1. Write a comprehensive plan based on the `<issue_description>` and the project review to the Plans Directory
2. Ask the user to review the plan and confirm or revise the plan.
3. If the user revises the plan, incorporate those into the `<issue_description>` and continue planning from step 1
4. If the user confirms the plan, incorporate the plan into the `<PLAN>` and continue with the features workflow

## Features Workflow

Incorporate the following steps into the `<PLAN>` as a top level item called 'Features'

1. Write comprehensive acceptance tests beneath the Features Directory that describes the desired outcome from addressing the `<issue_description>` and incorporating the plan
2. Ask the user to review the acceptance tests and confirm or revise the features.
3. If the user revises the features, incorporate those into the `<issue_description>` and continue features from step 1
4. If the user confirms the plan, continue with the implementation workflow

## Implementation Workflow

Incorporate the following steps into the `<PLAN>` as a top level item called 'Implementation'

1. Add steps to the implementation plan item in `<PLAN>` that comprehensively describes the steps to alter the source code in the repo to resolve `<issue_description>` according to the plan and features, such that the acceptance criteria are met.
2. Iteratively follow the steps in the implementation plan until all are complete.  Work sequentially.
3. Ask the user to review and confirm or revise the implementation
4. If the user provides revisions, incorporate those into the `<issue_description>` and continue implementation from step 1
5. If the user confirms the implementation, continue with the test workflow

## Test Workflow

Incorporate the following steps into the `<PLAN>` as a top level item called 'Test'

1. Ask the user if they wish to test the implementation.
2. If the user declines testing, continue with the Checkpoint workflow
3. If the user approves testing
    1. Update steps in the test plan item in the `<PLAN>` that describes any outstanding or failing tests
    2. If there are failing tests use context7 to retrieve documentation for technologies used to aid in the resolution of failing tests
    3. Consider amendments to the test code or implementation, but do not alter the features other than to clarify or otherwise improve the DSL.  Maintain the intent of the features.
    4. Ask the user for additional input if there are tests that are problematic to resolve (for example, mark the tests as pending rather than removing or faking a pass)
    5. If the user declines or aborts testing, continue with the checkpoint workflow
    6. When there are no failing tests, continue with the checkpoint workflow
4. Repeat the test workflow whilst failing tests remain

## Checkpoint Workflow

Incorporate the following steps into the `<PLAN>` as a top level item called 'Checkpoint'

1. Ask the user if the uncommitted changes should be committed to VCS
2. If the user reports a problem or provides a revision, address the new concern and continue with step 1 of the Test Workflow
3. If the user declines, end the workflow
4. If the user confirms, stage the changes, commit the changes with a commit message that adheres to the Commit Message Format, and end the workflow

# Plans Directory

- Use `<project_root>/plans`
- Create a plan in markdown format whose name is formed from the current date in YYYY-MM-DDTHH-mm format, plus a terse summarization of the `<issue_description>`, with `.md` extension

# Commit Message Format

- Create a concise title that describes the nature of the change
- Leave a blank line, and then describe the changes, using bullet points for multiple changes.
- Avoid overly verbose descriptions or unnecessary details.

# Reviewing the codebase

- Search for all files including dot directories, but excluding the following:
    - .idea directory
    - .git directory

# Ask the user

- Use the `ask_user` tool
- Provide the message in multiline plain text format
