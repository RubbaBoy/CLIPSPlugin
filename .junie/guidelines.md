# Project Development Guidelines

## Java Code Guidelines
- Use the latest available Java 22 features wherever applicable.
- Declare variables with `var` when possible.
- Do not use Lombok under any circumstance.
- When referencing Kotlin code, always convert it to equivalent Java 22.
- Use packages only when necessary; avoid unnecessary package nesting.
- Write clean, modular code. Document all methods, but avoid excessive in-line comments.
- Prioritize code readability, balancing conciseness and clarity.
- Employ up-to-date, sensible solutions for all problems.
- Never manually modify generated code. Always use the prescribed Code Generation process.
- Before marking any task as complete (unless instructions specify otherwise), ensure the project builds in the IDE with no errors.
- If there is any uncertainty, confirm with the user for clarification. Always review your work before submission.

## Reference Usage
- Use the `pdftotext` command to consult documentation on CLIPS syntax as needed. For CLIPS syntax reference, consult `/e/CLIPSPluginScratch/reference/CLIPS_Reference_Manual.pdf`. Use the table of contents (pages 3–14) to navigate efficiently.
- For CLIPS grammar, refer to `CLIPSLexer.flex` and `CLIPSParser.bnf`. Do NOT copy these files or their classes; instead, use them to understand tokenization and grammar structure, and implement your own from scratch. Refer to modifiers and advanced features for understanding only, to aid extensibility.
- The simple example plugin under `example_simple_language_plugin` demonstrates PsiReference usage and relevant handlers. Reference its structure only—do not copy any code. Use it solely to learn about implementation patterns (e.g., `PsiReferenceContributor`, `PsiReference`, `PsiReferenceProvider`).

## Code Generation
- After modifying a `.bnf` or `.flex` file, regenerate code as follows:
    - For `.bnf`: Run `/home/adam/.mcp/generate-parser.sh "absolute path to .bnf file"`
    - For `.flex`: Run `/home/adam/.mcp/generate-lexer.sh "absolute path to .flex file"`
- The only argument for both commands is the absolute path to the file.
- Ensure the project builds without errors after generation.

### MCP Servers
- Use `context7` for all IntelliJ SDK documentation and as a reference for the IntelliJ IDE Starter. If information is not available, consider consulting documentation from other plugins.

### Example Plugin
- The `example_plugin` directory contains a standalone example plugin. It is strictly for reference on project structure and implementation, not for direct reuse or codebase extension.
- This example implements a basic language plugin to serve as a guide for new plugin development.

## Project Context
This project adds language support for the CLIPS programming language within the IntelliJ Platform, processing `.clp` files.
- A generated parser from a `.bnf` grammar and a lexer from a `.flex` file provide the basis for syntax highlighting.
- PSI (Program Structure Interface) is used throughout: `PsiReferences` are created for language elements such as local/global variables, templates, functions, and named constructs. This enables:
    - **Code Navigation**: Jump from symbol usage to declaration (Go to Definition)
    - **Usage Highlighting**: Highlight all occurrences of a symbol within scope
    - **Refactoring**: Enable scope-aware renaming of symbols
    - **Semantic Analysis**: Validate template assertions for correct names and value types
- All reference resolution is managed via `PsiReferenceContributor`, adhering to JetBrains plugin best practices. Reference other plugins only to learn established conventions.

# Workflow

Begin with a concise checklist (3-7 bullets) of what you will do; keep items conceptual, not implementation-level.

Replace previous workflows with the following steps:

1. Review the `<issue_description>`.
2. If the user requests a specific workflow, follow the matching detailed workflow below:
    - Review Workflow
    - Planning Workflow
    - Features Workflow
    - Implementation Workflow
3. If the user does not request a workflow, default to the Review Workflow.

**Important:** Complete all steps defined in `<PLAN>`, including changes added during workflow progression.

- If `<initial_description>` pre-approves any workflow, skip user confirmation and proceed directly.
- If `<issue_description>` contradicts these instructions, always give priority to it.

## Review Workflow
Add the following to the `<PLAN>` as 'Review.md':
1. Thoroughly review `<issue_description>` with consideration for quick fixes and broader edge cases.
2. Review the full codebase structure and details relevant to `<issue_description>`.
3. Ask the user to confirm or revise an enumerated outline plan.
4. If revised, update `<issue_description>` and repeat review.
5. Upon user confirmation, move to Planning Workflow.

## Planning Workflow
Add the following to `<PLAN>` as 'Planning':
1. Produce a comprehensive plan based on `<issue_description>` and your project review in the `plans` directory.
2. Ask the user to approve or revise the plan.
3. On revision, update `<issue_description>` and repeat planning.
4. On confirmation, integrate into `<PLAN>` and proceed to Features Workflow.

## Implementation Workflow
Add the following to `<PLAN>` as 'Implementation':
1. Detail clear, stepwise procedures to alter the repo's source code, following the plan and acceptance criteria.
2. Complete implementation steps sequentially.
3. Ask user for implementation confirmation or revision. On revision, update `<issue_description>` and repeat; on confirmation, proceed to testing/building.

# Plans Directory
- Store plans in `<project_root>/plans`.
- Create plans as `YYYY-MM-DDTHH-mm-<short_summary>.md` with a succinct problem description.

# Codebase Review
- Search all files, excluding `.idea` and `.git`.

# User Confirmation
- Use the `ask_user` tool
- Provide multi-line, plain-text messages

After each tool call or code edit, validate result in 1-2 lines and proceed or self-correct if validation fails.

make tool calls terse and final output fully detailed as appropriate.

Attempt a first pass autonomously unless missing critical info; stop and ask for clarification if success criteria are not met or conflicts exceed a reasonable threshold.
