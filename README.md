# Walton Sustainability Centre

## Background
Each month my employer gives us a dedicated *free-play / learning* day. With the arrival of **JetBrains Junie**, I’m using 
today's (29 April 2025) free-play to see how well Junie handles a project where the **requirements themselves live under source control 
right alongside the code**.

The testbed is a [set of requirements my MSc tutor group wrote back in 2017](https://github.com/Open-University-PG-SWEng/M814-16K-Foley) (back when I was studying for my Masters): a set of functional 
and non-functional requirements for a fictional **Sustainability Centre**. By resurrecting that spec—and tracking every 
edit in Git—I can judge how Junie copes when the full story (code *and* evolving requirements) sits in one repo.

## About Walton Sustainability Centre

Walton Sustainability Centre is a fictional community-focused education venue in the UK that promotes sustainable living 
through practical, hands-on workshops. The Centre:

- **Delivers scheduled, fee-paying workshops** open to the public.
- **Runs an annual membership scheme** that grants discounted booking rates and other perks.
- **Processes payments in-house**, supporting card transactions and bank transfers while keeping invoicing up-to-date.
- **Maintains a small inventory** of teaching equipment and consumables, raising purchase orders as required.
- **Produces routine and ad-hoc management reports** for staff, trustees and auditors.
- **Ensures full compliance with the UK Data Protection Act**, enforcing strict access controls around personal and payment data.

This repository holds the functional and non-functional requirements for the information system that automates those day-to-day operations.
Feel free to drop this block straight into your README.md to give contributors a clear, concise sense of what

| Note                                                                     |
|--------------------------------------------------------------------------|
| Chat-GPT managed to glean this description by scanning the requirements. |

## Where to find the requirements

| Scope | Location | Format |
|-------|----------|--------|
| **Functional requirements** | `WaltonSustainabilityCentre-Requirements.txt` (repository root) | Single plain-text file, one requirement per “REQ-ID” entry. :contentReference[oaicite:0]{index=0} |
| **Non-functional requirements** | `NonfunctionalRequirements/` | Folder of plain-text files (`NFR-x-*.txt`), one file per requirement plus a template for new items. :contentReference[oaicite:1]{index=1} |

Both sets are human-readable text so they can be diffed and reviewed like code. Any future updates should follow the same 
naming conventions (see example below) to keep the documentation consistent.

```text
REQ-ID:
Description:
Type:
Rationale:
Fit Criterion:
Originator:
Comments: [M814-ModuleChair] Add comments below, making sure to include your initials in [] at the start.
```

::contentReference[oaicite:2]{index=2}