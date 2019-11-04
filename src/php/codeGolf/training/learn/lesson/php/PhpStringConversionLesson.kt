package php.codeGolf.training.learn.lesson.php

import php.codeGolf.training.learn.interfaces.Module
import php.codeGolf.training.learn.lesson.kimpl.KLesson
import php.codeGolf.training.learn.lesson.kimpl.LessonContext
import php.codeGolf.training.learn.lesson.kimpl.parseLessonSample

class PhpStringConversionLesson(module: Module) : KLesson("String Conversions", module, "PHP") {
    private val sample1 = parseLessonSample("""<?php

${'$'}task = "The Not-So-Universal Question";
${'$'}answer = 43;

echo "The answer for " . <caret>${'$'}task . " is the " . ${'$'}answer;
""".trimIndent())

    private val sample2 = parseLessonSample("""
<?php

${'$'}task = "The Not-So-Universal Question";
${'$'}answer = 43;

echo sprintf("The answer <caret>for %s is the %d", ${'$'}task, ${'$'}answer);
    """.trimIndent())
    private val sample3 = parseLessonSample("""<?php

${'$'}task = "The Not-So-Universal Question";
${'$'}answer = 43;

echo "The answer for {${'$'}ta<caret>sk} is the {${'$'}answer}"; """.trimIndent())

    override val lessonContent: LessonContext.() -> Unit
        get() = {
            prepareSample(sample1)
            task {
                text("PhpStorm has a set of intentions to switch a string literal between different presentations: " +
                        "concatenation, interpolation, sprintf, HEREDOC and NOWDOC. To use them, press " +
                        "<action>ShowIntentionActions</action> and select <strong>Convert concatenation to sprintf call</strong>.")
                trigger("ShowIntentionActions",
                        { editor.document.text },
                        { before, now -> now.contains("sprintf") && now != before })
            }
            waitBeforeContinue(500)
            prepareSample(sample2)
            task {
                text("There are more transformations to try. Press <action>ShowIntentionActions</action> one more" +
                        "time and choose <strong>Convert sprintf call to string interpolation</strong>.")
                trigger("ShowIntentionActions",
                        { editor.document.text },
                        { before, now -> now.contains("{${'$'}task}") && now != before })
            }
            waitBeforeContinue(500)
            prepareSample(sample3)
            task {
                text("Press <action>ShowIntentionActions</action> one more time and choose " +
                        "<strong>Convert string literal to HEREDOC</strong>.")
                trigger("ShowIntentionActions",
                        { editor.document.text },
                        { before, now -> now.contains("<<<") && now != before })
            }
        }
}