package suhockii.dev.bookfinder.data.progress

import suhockii.dev.bookfinder.R

enum class ProgressStep {
    DOWNLOADING {
        override val number: Int = 1
        override val descriptionRes = R.string.downloading
        override var progress: Int = 0
    },
    UNZIPPING {
        override val number: Int = 2
        override val descriptionRes = R.string.unzipping
        override var progress: Int = 0
    },
    ANALYZING {
        override val number: Int = 3
        override val descriptionRes = R.string.analyzing
        override var progress: Int = 0
    },
    PARSING {
        override val number: Int = 4
        override val descriptionRes = R.string.parsing
        override var progress: Int = 0
    },
    SAVING {
        override val number: Int = 5
        override val descriptionRes = R.string.saving
        override var progress: Int = 0

    };

    var isAllCompleted = false
    abstract val number: Int
    abstract val descriptionRes: Int
    abstract var progress: Int
}