package app.suhocki.mybooks.data.error

import app.suhocki.mybooks.R

enum class ErrorType {
    NETWORK {
        override val descriptionRes = R.string.error_network
    },
    OUT_OF_MEMORY {
        override val descriptionRes = R.string.out_of_memory
    },
    CORRUPTED_FILE {
        override val descriptionRes = R.string.corrupted_file
    },
    UNKNOWN {
        override val descriptionRes = R.string.error_unknown
    };

    abstract val descriptionRes: Int
}