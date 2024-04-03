package com.azadmehr.meysam.kmqtt_broker

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mqtt.Subscription
import mqtt.broker.Broker
import mqtt.broker.interfaces.PacketInterceptor
import mqtt.getSharedTopicShareName
import mqtt.packets.MQTTPacket
import mqtt.packets.mqtt.MQTTAuth
import mqtt.packets.mqtt.MQTTConnect
import mqtt.packets.mqtt.MQTTPublish
import mqtt.packets.mqtt.MQTTSubscribe

class MainActivity : AppCompatActivity() {
    @ExperimentalUnsignedTypes
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        val broker = Broker(packetInterceptor = object : PacketInterceptor {
            override fun packetReceived(
                clientId: String,
                username: String?,
                password: UByteArray?,
                packet: MQTTPacket
            ) {
                when (packet) {
                    is MQTTConnect -> Log.e("TAG", "packetReceived: ${packet.clientID}")
                    is MQTTPublish -> Log.e("TAG:", packet.topicName)
                    is MQTTSubscribe -> Log.e("TAG:", "subscribe new client $clientId")
                }
            }
        })

        CoroutineScope(Dispatchers.IO).launch {
            val subscription = Subscription(
                topicFilter = "/test-topic"
            )
            broker.addSubscription(clientId = "test", subscription = subscription)
            broker.listen() // Blocking method, use step() if you don't want to block the ohread
        }
    }
}