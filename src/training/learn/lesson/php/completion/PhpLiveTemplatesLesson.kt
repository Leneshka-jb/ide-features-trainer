package training.learn.lesson.php.completion

import com.intellij.openapi.editor.Editor
import com.intellij.openapi.util.Condition
import com.intellij.psi.PsiElement
import com.jetbrains.php.lang.psi.PhpPsiUtil
import com.jetbrains.php.lang.psi.elements.ForeachStatement
import com.jetbrains.php.lang.psi.elements.GroupStatement
import com.jetbrains.php.lang.psi.elements.PhpEchoStatement
import training.learn.interfaces.Module
import training.learn.lesson.kimpl.KLesson
import training.learn.lesson.kimpl.LessonContext
import training.learn.lesson.kimpl.parseLessonSample

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
                text("The IDE can offer code snippets, called Live Templates. Type ${code("eco")}, get completion and press ${action("EditorEnter")}.")
                trigger("EditorChooseLookupItem") {
                    checkHasElement(editor, (Condition { use: PsiElement -> use is PhpEchoStatement }))
                }
            }
            waitBeforeContinue(500)
            prepareSample(sample2)
            task {
                text("All available samples one can see and configure at Settings | Editor | Live Templates.\n" +
                        "For next example type ${code("fore")}, get completion and press ${action("EditorEnter")}.")
                trigger("EditorChooseLookupItem") {
                    checkHasElement(editor, ForeachStatement.INSTANCEOF)
                }
            }
        }

    private fun checkHasElement(editor: Editor, condition: Condition<PsiElement>): Boolean {
        val psiFile = getPhpFile(editor) ?: return false
        val groupStatement = PhpPsiUtil.getChildByCondition<GroupStatement>(psiFile, GroupStatement.INSTANCEOF)
        val element = PhpPsiUtil.getChildByCondition<PhpEchoStatement>(groupStatement, condition)
        return element != null
    }
}