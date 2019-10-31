package training.learn.lesson.php.completion

import com.intellij.openapi.editor.Editor
import com.intellij.openapi.fileEditor.FileDocumentManager
import com.intellij.psi.PsiManager
import com.intellij.testGuiFramework.framework.GuiTestUtil.shortcut
import com.intellij.testGuiFramework.framework.GuiTestUtil.typeText
import com.intellij.testGuiFramework.impl.jList
import com.intellij.testGuiFramework.util.Key
import com.jetbrains.php.lang.psi.PhpFile
import com.jetbrains.php.lang.psi.PhpPsiUtil
import training.learn.interfaces.Module
import training.learn.lesson.kimpl.KLesson
import training.learn.lesson.kimpl.LessonContext
import training.learn.lesson.kimpl.parseLessonSample

class PhpBasicCompletionsLesson(module: Module) : KLesson("Basic Completions", module, "PHP") {
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
                test {
                    ideFrame {
                        typeText("pu")
                        jList("purrr")
                        shortcut(Key.ENTER)
                    }
                }
            }
            waitBeforeContinue(500)
            prepareSample(sample2)
            task("CodeCompletion") {
                text("To activate Basic Completion explicitly, press ${action(it)}. " +
                        "Select <code>meow()</code> and press <action>EditorEnter</action>.")
                trigger(it)
                trigger("EditorChooseLookupItem") { textBeforeCaret(editor, "meow();") }
                test {
                    actions(it)
                    ideFrame {
                        jList("meow")
                        shortcut(Key.ENTER)
                    }
                }
            }
            waitBeforeContinue(500)
            prepareSample(sample3)
            actionTask("CodeCompletion") { "Press ${action(it)}to show completion options." }
            task("EditorChooseLookupItemReplace") {
                text("Choose <code>bringToy()</code>, for example, and press ${action("EditorTab")}.\n" +
                        "This overwrites the word at the caret rather than simply inserting it.")
                trigger(it)
                trigger("EditorChooseLookupItemReplace") { textBeforeCaret(editor, "bringToy()") }
                test {
                    actions(it)
                    ideFrame {
                        jList("bringToy")
                        shortcut(Key.ENTER)
                    }
                }
            }
        }

    private fun checkPurrInvocation(editor: Editor): Boolean {
        val project = editor.project ?: return false
        val file = FileDocumentManager.getInstance().getFile(editor.document) ?: return false
        val psiFile = PsiManager.getInstance(project).findFile(file)
        if (psiFile !is PhpFile) return false
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