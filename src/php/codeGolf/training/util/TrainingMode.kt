package php.codeGolf.training.util

enum class TrainingMode {
  NORMAL {
    override val doesShowResetButton = false
  },
  DEMO {
    override val doesShowResetButton = true
  },
  DEVELOPMENT {
    override val doesShowResetButton = true
  };

  abstract val doesShowResetButton: Boolean
}