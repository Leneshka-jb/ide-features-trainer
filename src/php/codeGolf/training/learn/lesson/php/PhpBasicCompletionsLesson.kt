package php.codeGolf.training.learn.lesson.php

import com.intellij.openapi.editor.Editor
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.psi.PsiManager
import com.jetbrains.php.lang.psi.PhpFile
import com.jetbrains.php.lang.psi.PhpPsiUtil
import php.codeGolf.training.learn.interfaces.Module
import php.codeGolf.training.learn.lesson.kimpl.KLesson
import php.codeGolf.training.learn.lesson.kimpl.LessonContext
import php.codeGolf.training.learn.lesson.kimpl.parseLessonSample

class PhpBasicCompletionsLesson(module: Module) : KLesson("Basic Ways of Completion", module, "PHP") {
    private val sample1 = parseLessonSample("""<?php

class Cat
{
    function purrr()
    {
    }

    function meow()
    {
    }

    function usualDay()
    {
        <caret>
    }
}""".trimIndent())

    private val sample2 = parseLessonSample("""<?php

class Cat
{
    function purrr()
    {
    }

    function meow()
    {
    }

    function usualDay()
    {
        ${'$'}this->purrr();
        <caret>
    }
}
""".trimIndent())
    private val sample3 = parseLessonSample("""<?php

class Cat
{
    function purrr()
    {
    }

    function meow()
    {
    }

    function bringToy()
    {
    }

    function breakVase()
    {
    }

    function usualDay()
    {
        ${'$'}this->purrr();
        ${'$'}this->meow();
        ${'$'}this->br<caret>eakVase();
    }
}
""".trimIndent())


    override val lessonContent: LessonContext.() -> Unit
        get() = {
            prepareSample(sample1)
            task {
                text("By default, the IDE completes your code instantly. Start typing <code>pu</code> right where " +
                        "the caret is, and you will see the Lookup Menu with matching suggestions. Choose the first item " +
                        "<code>purrr()</code> from the Lookup menu by pressing <action>EditorEnter</action>.")
                trigger("EditorChooseLookupItem",
                        { editor.document.text },
                        { before, now -> checkPurrInvocation(editor) && now != before })
            }
            waitBeforeContinue(500)
            prepareSample(sample2)
            task("CodeCompletion") {
                text("To activate Basic Completion explicitly, press ${action(it)}. " +
                        "Select <code>meow()</code> and press <action>EditorEnter</action>.")
                trigger(it)
                trigger("EditorChooseLookupItem") { textBeforeCaret(editor, "meow();") }
            }
            waitBeforeContinue(500)
            prepareSample(sample3)
            actionTask("CodeCompletion") { "Press ${action(it)}to show completion options." }
            task("EditorChooseLookupItemReplace") {
                text("Choose <code>bringToy()</code>, for example, and press ${action("EditorTab")}. " +
                        "This overwrites the word at the caret rather than simply inserting it.")
                trigger(it)
                trigger("EditorChooseLookupItemReplace") { textBeforeCaret(editor, "bringToy()") }
            }
        }

    private fun checkPurrInvocation(editor: Editor): Boolean {
        val psiFile = getPhpFile(editor) ?: return false
        val catClass = PhpPsiUtil.findClass(psiFile) { clazz -> clazz.name == "Cat" } ?: return false
        val mainMethod = catClass.methods.first { method -> method.name == "usualDay" } ?: return false
        return mainMethod.text.contains("${'$'}this->purrr()")
    }

    private fun textBeforeCaret(editor: Editor, @Suppress("SameParameterValue") text: String): Boolean {
        val offset = editor.caretModel.offset
        if (offset < text.length) {
            return false
        }
        val subSequence = editor.document.charsSequence.subSequence(offset - text.length, offset)
        return subSequence.toString() == text
    }
}

fun getPhpFile(editor: Editor): PhpFile? {
    val project = editor.project ?: return null
    val file = FileDocumentManager.getInstance().getFile(editor.document) ?: return null
    val psiFile = PsiManager.getInstance(project).findFile(file)
    if (psiFile !is PhpFile) return null
    return psiFile
}