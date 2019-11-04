package php.codeGolf.training.learn.lesson.go.completion

import org.intellij.lang.annotations.Language
import php.codeGolf.training.lang.GoLangSupport
import php.codeGolf.training.learn.interfaces.Module
import php.codeGolf.training.learn.lesson.general.CompletionWithTabLesson
import php.codeGolf.training.learn.lesson.kimpl.parseLessonSample

class GoCompletionWithTabLesson(module: Module) : CompletionWithTabLesson(module, GoLangSupport.lang, "Errorf") {

  @Language("go")
  override val sample = parseLessonSample("""package main

import (
	"fmt"
)

func main() {
	fmt.<caret>Printf("hello world")
}
""")

}