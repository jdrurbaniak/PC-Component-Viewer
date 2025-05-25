package com.example.pccomponentviewer.data

import androidx.annotation.DrawableRes
import androidx.annotation.StringRes
import com.example.pccomponentviewer.R

sealed class PCComponent {
    abstract val id: Int
    abstract val name: String
    @get:DrawableRes
    abstract val imageResourceId: Int
    abstract val componentType: ComponentType
    abstract val videoUrl: String?
}

enum class ComponentType {
    ALL,
    RAM,
    STORAGE,
    GRAPHICS_CARD
}


data class RAM(
    override val id: Int,
    override val name: String,
    @DrawableRes override val imageResourceId: Int,
    val size: Int,
    val manufacturer: String,
    val type: RAMType,
    val frequency: Int,
    val timing: String,
    override val videoUrl: String? = null,
    override val componentType: ComponentType = ComponentType.RAM
) : PCComponent()

enum class RAMType {
    DDR2, DDR3, DDR4, DDR5
}

data class Storage(
    override val id: Int,
    override val name: String,
    @DrawableRes override val imageResourceId: Int,
    val type: StorageType,
    val capacity: Int,
    val manufacturer: String,
    val readSpeed: Int?,
    val writeSpeed: Int?,
    val rpm: Int?,
    override val videoUrl: String? = null,
    override val componentType: ComponentType = ComponentType.STORAGE
) : PCComponent()

enum class StorageType {
    SSD_NVME, SSD_SATA, HDD
}

data class GraphicsCard(
    override val id: Int,
    override val name: String,
    @DrawableRes override val imageResourceId: Int,
    val manufacturer: String,
    val vramSize: Int,
    val clockSpeed: Int,
    override val videoUrl: String? = null,
    override val componentType: ComponentType = ComponentType.GRAPHICS_CARD
) : PCComponent()

object PCComponents {
    val components = listOf(
        RAM(
            id = 1,
            name = "Corsair Vengeance RGB Pro",
            imageResourceId = R.drawable.placeholder_ram_4gb,
            size = 32,
            manufacturer = "Corsair",
            type = RAMType.DDR4,
            frequency = 3600,
            timing = "CL18",
            videoUrl = "testvideo"
        ),
        RAM(
            id = 2,
            name = "G.Skill Trident Z5",
            imageResourceId = R.drawable.placeholder_ram_8gb,
            size = 32,
            manufacturer = "G.Skill",
            type = RAMType.DDR5,
            frequency = 6000,
            timing = "CL36"
        ),
        Storage(
            id = 3,
            name = "Samsung 970 EVO Plus",
            imageResourceId = R.drawable.placeholder_ram_4gb,
            type = StorageType.SSD_NVME,
            capacity = 1000,
            manufacturer = "Samsung",
            readSpeed = 3500,
            writeSpeed = 3300,
            rpm = null
        ),
        Storage(
            id = 4,
            name = "Seagate Barracuda",
            imageResourceId = R.drawable.placeholder_ram_4gb,
            type = StorageType.HDD,
            capacity = 2000,
            manufacturer = "Seagate",
            readSpeed = null,
            writeSpeed = null,
            rpm = 7200
        ),
        GraphicsCard(
            id = 5,
            name = "NVIDIA GeForce RTX 4080",
            imageResourceId = R.drawable.placeholder_ram_4gb,
            manufacturer = "NVIDIA",
            vramSize = 16,
            clockSpeed = 2505
        ),
        RAM(
            id = 6,
            name = "Corsair Dominator Platinum RGB",
            imageResourceId = R.drawable.placeholder_ram_4gb,
            size = 32,
            manufacturer = "Corsair",
            type = RAMType.DDR5,
            frequency = 6000,
            timing = "CL30"
        ),
        RAM(
            id = 7,
            name = "Kingston Fury Beast",
            imageResourceId = R.drawable.placeholder_ram_4gb,
            size = 64,
            manufacturer = "Kingston",
            type = RAMType.DDR5,
            frequency = 5200,
            timing = "CL40"
        ),
        Storage(
            id = 8,
            name = "Samsung 990 PRO",
            imageResourceId = R.drawable.placeholder_ram_4gb,
            type = StorageType.SSD_NVME,
            capacity = 2000,
            manufacturer = "Samsung",
            readSpeed = 7450,
            writeSpeed = 6900,
            rpm = null
        ),
        Storage(
            id = 9,
            name = "Seagate IronWolf Pro",
            imageResourceId = R.drawable.placeholder_ram_4gb,
            type = StorageType.HDD,
            capacity = 8000,
            manufacturer = "Seagate",
            readSpeed = null,
            writeSpeed = null,
            rpm = 7200
        ),
        GraphicsCard(
            id = 10,
            name = "AMD Radeon RX 7800 XT",
            imageResourceId = R.drawable.placeholder_ram_4gb,
            manufacturer = "AMD",
            vramSize = 16,
            clockSpeed = 2430
        )
    )

    fun getComponentById(id: Int): PCComponent {
        return components.first { it.id == id }
    }
}
