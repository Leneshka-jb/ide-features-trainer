package php.codeGolf.training.learn.lesson.go.completion

import php.codeGolf.training.lang.GoLangSupport
import php.codeGolf.training.learn.interfaces.Module
import php.codeGolf.training.learn.lesson.kimpl.KLesson

abstract class GoLesson(name: String, module: Module) : KLesson(name, module, GoLangSupport.lang)
