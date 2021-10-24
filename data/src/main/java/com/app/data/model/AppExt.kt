package com.app.data.model

import com.app.data.utils.Resource
import kotlinx.coroutines.flow.Flow

typealias FlowResource<T> = Flow<Resource<T>>
typealias FlowState = FlowResource<Any?>
