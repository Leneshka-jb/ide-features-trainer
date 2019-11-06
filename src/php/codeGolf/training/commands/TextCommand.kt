package php.codeGolf.training.commands

import php.codeGolf.training.learn.lesson.LessonManager
import php.codeGolf.training.ui.Message

/**
 * Created by karashevich on 30/01/15.
 */
class TextCommand : Command(Command.CommandType.TEXT) {

  @Throws(InterruptedException::class)
  override fun execute(executionList: ExecutionList) {


    val element = executionList.elements.poll()
    val lesson = executionList.lesson

    LessonManager.instance.addMessages(Message.convert(element))

    startNextCommand(executionList)

  }

}