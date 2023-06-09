package com.example.saluslink.ui.message_recycler_view.view_holders

import com.example.saluslink.ui.message_recycler_view.views.MessageView

interface MessageHolderInterface {
    fun drawMessage(view: MessageView)
    fun onAttach(view: MessageView)
    fun onDetach()
}