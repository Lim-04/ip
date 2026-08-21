# UI Test Plan

This file defines the test cases used by the `test-ui` skill (see
`.claude/skills/test-ui/SKILL.md`) to verify XiaoZhi's console behavior.

Each test case gives the exact lines fed to the program on stdin, in order,
and the exact console output the program is expected to produce in return
(including the banner/greeting/farewell, so the whole session is checked,
not just the feature under test). Add a new test case here whenever you add
or change a command, and re-run the `test-ui` skill before committing.

## Test 1: Greet and exit

**Aim:** Verify the banner, greeting, and farewell all print correctly, and
the program exits immediately on `bye` without doing anything else.

**Input:**
```
bye
```

**Expected output:**
```
__  ___            ______     _
\ \/ (_) __ _  ___|__  / |__ (_)
 \  /| |/ _` |/ _ \ / /| '_ \| |
 /  \| | (_| | (_) / /_| | | | |
/_/\_\_|\__,_|\___/____|_| |_|_|

Hi! I'm XiaoZhi.
What's the task for today?
Bye, See you soon!
```

## Test 2: Add a todo

**Aim:** Verify a `todo` command stores the task and confirms with the
`[T]` tag, and that the type keyword is stripped from the description.

**Input:**
```
todo read book
bye
```

**Expected output:**
```
__  ___            ______     _
\ \/ (_) __ _  ___|__  / |__ (_)
 \  /| |/ _` |/ _ \ / /| '_ \| |
 /  \| | (_| | (_) / /_| | | | |
/_/\_\_|\__,_|\___/____|_| |_|_|

Hi! I'm XiaoZhi.
What's the task for today?
Got it. I've added this task:
  [T][ ] read book
Now you have 1 tasks in the list.
Bye, See you soon!
```

## Test 3: Add a deadline

**Aim:** Verify a `deadline` command splits off the `/by` portion and shows
it as `(by: ...)` with the `[D]` tag.

**Input:**
```
deadline return book /by Sunday
bye
```

**Expected output:**
```
__  ___            ______     _
\ \/ (_) __ _  ___|__  / |__ (_)
 \  /| |/ _` |/ _ \ / /| '_ \| |
 /  \| | (_| | (_) / /_| | | | |
/_/\_\_|\__,_|\___/____|_| |_|_|

Hi! I'm XiaoZhi.
What's the task for today?
Got it. I've added this task:
  [D][ ] return book (by: Sunday)
Now you have 1 tasks in the list.
Bye, See you soon!
```

## Test 4: Add an event

**Aim:** Verify an `event` command splits off both the `/from` and `/to`
portions and shows them as `(from: ... to: ...)` with the `[E]` tag.

**Input:**
```
event project meeting /from Mon 2pm /to 4pm
bye
```

**Expected output:**
```
__  ___            ______     _
\ \/ (_) __ _  ___|__  / |__ (_)
 \  /| |/ _` |/ _ \ / /| '_ \| |
 /  \| | (_| | (_) / /_| | | | |
/_/\_\_|\__,_|\___/____|_| |_|_|

Hi! I'm XiaoZhi.
What's the task for today?
Got it. I've added this task:
  [E][ ] project meeting (from: Mon 2pm to: 4pm)
Now you have 1 tasks in the list.
Bye, See you soon!
```

## Test 5: List multiple tasks of different types

**Aim:** Verify `list` prints every stored task, numbered from 1, each
rendered with its own type's format, in the order they were added.

**Input:**
```
todo read book
deadline return book /by Sunday
event project meeting /from Mon 2pm /to 4pm
list
bye
```

**Expected output:**
```
__  ___            ______     _
\ \/ (_) __ _  ___|__  / |__ (_)
 \  /| |/ _` |/ _ \ / /| '_ \| |
 /  \| | (_| | (_) / /_| | | | |
/_/\_\_|\__,_|\___/____|_| |_|_|

Hi! I'm XiaoZhi.
What's the task for today?
Got it. I've added this task:
  [T][ ] read book
Now you have 1 tasks in the list.
Got it. I've added this task:
  [D][ ] return book (by: Sunday)
Now you have 2 tasks in the list.
Got it. I've added this task:
  [E][ ] project meeting (from: Mon 2pm to: 4pm)
Now you have 3 tasks in the list.
Tasks for today:
1.[T][ ] read book
2.[D][ ] return book (by: Sunday)
3.[E][ ] project meeting (from: Mon 2pm to: 4pm)
Bye, See you soon!
```

## Test 6: Mark and unmark a task

**Aim:** Verify `mark <n>` sets the task's status icon to `X` and `unmark
<n>` clears it back, using 1-based indices as shown by `list`.

**Input:**
```
todo read book
mark 1
unmark 1
bye
```

**Expected output:**
```
__  ___            ______     _
\ \/ (_) __ _  ___|__  / |__ (_)
 \  /| |/ _` |/ _ \ / /| '_ \| |
 /  \| | (_| | (_) / /_| | | | |
/_/\_\_|\__,_|\___/____|_| |_|_|

Hi! I'm XiaoZhi.
What's the task for today?
Got it. I've added this task:
  [T][ ] read book
Now you have 1 tasks in the list.
Roger! I've marked it as done: 
  [T][X] read book
Okay, I've unmarked this: 
  [T][ ] read book
Bye, See you soon!
```

## Test 7: Unrecognised command

**Aim:** Verify a command that isn't `list`/`mark`/`unmark`/`todo`/
`deadline`/`event`/`bye` is reported back to the user instead of crashing
or being silently stored.

**Input:**
```
foobar
bye
```

**Expected output:**
```
__  ___            ______     _
\ \/ (_) __ _  ___|__  / |__ (_)
 \  /| |/ _` |/ _ \ / /| '_ \| |
 /  \| | (_| | (_) / /_| | | | |
/_/\_\_|\__,_|\___/____|_| |_|_|

Hi! I'm XiaoZhi.
What's the task for today?
Sorry, I don't recognise that command: foobar
Bye, See you soon!
```
