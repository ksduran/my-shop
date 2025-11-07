package com.kevinduran.myshop.config.extensions

/**
 * Sanitizes color names to be filesystem and remote-path friendly.
 *
 * It trims leading and trailing whitespace and replaces one or more
 * internal whitespace characters with underscores. This keeps
 * user-entered color values intact while ensuring the generated
 * paths remain valid for local and remote storage.
 */
fun String.sanitizeColor(): String = trim().replace("\\s+".toRegex(), "_")