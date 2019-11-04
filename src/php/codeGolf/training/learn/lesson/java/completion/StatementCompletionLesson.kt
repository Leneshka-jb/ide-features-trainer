package php.codeGolf.training.learn.lesson.java.completion

import com.intellij.psi.PsiDocumentManager
import com.intellij.psi.PsiForStatement
import com.intellij.psi.util.PsiTreeUtil
import php.codeGolf.training.commands.kotlin.TaskContext
import php.codeGolf.training.lang.JavaLangSupport
import php.codeGolf.training.learn.interfaces.Module
import php.codeGolf.training.learn.lesson.kimpl.KLesson
import php.codeGolf.training.learn.lesson.kimpl.LessonContext
import php.codeGolf.training.learn.lesson.kimpl.parseLessonSample

class StatementCompletionLesson(module: Module): KLesson("Statement Completion", module, JavaLangSupport.lang) {

  val sample = parseLessonSample("""class PrimeNumbers {
    public static void main(String[] args) {
        System.out.println("Prime numbers between 1 and 100");

        for (int i = 2; i < 100; i++) {
            boolean isPrime = true;

            for (int j = 2; j < i; j++)

            if (isPrime) {
                System.out.print(i + " ");
            }
        }
    }
}""".trimIndent())

  override val lessonContent: LessonContext.() -> Unit = {
    prepareSample(sample)
    caret(8, 40)
    actionTask("EditorCompleteStatement") {
      "Press ${action(it)} to complete the <code>for</code> statement."
    }
    task("EditorCompleteStatement") {
      text("Write <code>if</code> and press ${action(it)} to generate the statement.")
      stateCheck {
        return@stateCheck checkIfAppended()
      }
    }
    actionTask("EditorCompleteStatement") {
      "Add a condition inside parentheses <code>i % j == 0</code> and press ${action(it)} to jump inside the <code>if</code> statement."
    }
    actionTask("EditorCompleteStatement") {
      "Write on one line: <code>isPrime = false; break</code> and then press ${action(it)} to complete the entered statement and apply formatting."
    }
  }

  private fun TaskContext.checkIfAppended(): Boolean {
    val psiFile = PsiDocumentManager.getInstance(project).getPsiFile(editor.document)
    val psiForStatements = PsiTreeUtil.findChildrenOfType(psiFile, PsiForStatement::class.java).toTypedArray()
    if (psiForStatements.size < 2) return false

    val psiForStatement = psiForStatements[1] as PsiForStatement

    val text = psiForStatement.body!!.text
    val trimmedText = text.replace("\\s+".toRegex(), "")

    return trimmedText == "{if(){}}"
  }
}