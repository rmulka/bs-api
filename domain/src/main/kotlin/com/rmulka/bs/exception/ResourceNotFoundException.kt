package com.rmulka.bs.exception

import java.lang.RuntimeException

class ResourceNotFoundException(message: String) : RuntimeException(message)