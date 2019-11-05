package php.codeGolf.training.learn.lesson.php

import com.intellij.openapi.editor.Editor
import com.intellij.openapi.util.Condition
import com.intellij.psi.PsiElement
import com.jetbrains.php.lang.psi.PhpPsiUtil
import com.jetbrains.php.lang.psi.elements.ForeachStatement
import com.jetbrains.php.lang.psi.elements.GroupStatement
import com.jetbrains.php.lang.psi.elements.PhpEchoStatement
import php.codeGolf.training.learn.interfaces.Module
import php.codeGolf.training.learn.lesson.kimpl.KLesson
import php.codeGolf.training.learn.lesson.kimpl.LessonContext
import php.codeGolf.training.learn.lesson.kimpl.parseLessonSample

class PhpLiveTemplatesLesson(module: Module) : KLesson("Live Templates", module, "PHP") {
    private val sample = parseLessonSample("""<?php

<caret>
""".trimIndent())

    private val sample2 = parseLessonSample("""<?php

echo ";)";

${'$'}elements = [1, "Blue", array(), 5];

<caret>"""
    )

    override val lessonContent: LessonContext.() -> Unit
        get() = {
            prepareSample(sample)
            task {
                text("The IDE offers code snippets, called Live Templates. Type ${code("eco")} and press ${action("EditorEnter")}.")
                trigger("EditorChooseLookupItem") {
                    checkHasElement(editor, (Condition { use: PsiElement -> use is PhpEchoStatement }))
                }
            }
            waitBeforeContinue(500)
            prepareSample(sample2)
            task {
                text("You can view and configure the available snippets at Settings | Editor | Live Templates.\n" +
                        "Now, type ${code("fore")} and press ${action("EditorEnter")}.")
                trigger("EditorChooseLookupItem") {
                    checkHasElement(editor, ForeachStatement.INSTANCEOF)
                }
            }
        }

    private fun checkHasElement(editor: Editor, condition: Condition<PsiElement>): Boolean {
        val psiFile = getPhpFile(editor) ?: return false
        val groupStatement = PhpPsiUtil.getChildByCondition<GroupStatement>(psiFile, GroupStatement.INSTANCEOF)
        val element = PhpPsiUtil.getChildByCondition<PsiElement>(groupStatement, condition)
        return element != null
    }
}