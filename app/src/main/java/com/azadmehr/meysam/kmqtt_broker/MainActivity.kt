package com.azadmehr.meysam.kmqtt_broker

import android.os.Bundle
import android.util.Log
import androidx.appcompat.app.AppCompatActivity
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.launch
import mqtt.broker.Broker
import mqtt.broker.interfaces.PacketInterceptor
import mqtt.packets.MQTTPacket
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
                    is MQTTConnect -> Log.e("TAG", "packetReceived: ${packet.protocolName}")
                    is MQTTPublish -> Log.e("TAG:", packet.topicName)
                    is MQTTSubscribe -> Log.e("TAG:", "subscribe new client")
                }
            }
        })

        CoroutineScope(Dispatchers.IO).launch {
            broker.listen() // Blocking method, use step() if you don't want to block the thread
        }
    }
}