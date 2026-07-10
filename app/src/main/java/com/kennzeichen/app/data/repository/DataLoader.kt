package com.kennzeichen.app.data.repository

import android.content.Context
import com.kennzeichen.app.data.database.KennzeichenEntity
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.withContext
import java.io.InputStreamReader
import java.nio.charset.StandardCharsets

class DataLoader(private val context: Context) {

    suspend fun loadKennzeichenData(): List<KennzeichenEntity> = withContext(Dispatchers.IO) {
        val inputStream = context.assets.open("kennzeichen.csv")
        val reader = InputStreamReader(inputStream, StandardCharsets.UTF_8)
        val csvContent = reader.readText()
        reader.close()

        val lines = csvContent.trim().split("\n")
        if (lines.isEmpty()) return@withContext emptyList()

        val headers = lines[0].split(";").map { it.trim() }
        val headerMap = headers.withIndex().associate { it.value to it.index }

        val dataLines = lines.drop(1)
        
        dataLines.mapNotNull { line ->
            try {
                val row = parseCsvLine(line)
                if (row.size < headers.size) return@mapNotNull null

                val kuerzel = getCell(row, headerMap, "Kürzel")?.trim() ?: return@mapNotNull null
                val stadt = getCell(row, headerMap, "Stadt")?.trim() ?: ""
                val bundesland = getCell(row, headerMap, "Bundesland")?.trim() ?: ""
                val typ = getCell(row, headerMap, "Typ")?.trim() ?: ""

                val bevoelkerung = parseInt(getCell(row, headerMap, "Bevölkerung"))
                val flaeche = parseDouble(getCell(row, headerMap, "Fläche"))
                val kennzeichenStart = getCell(row, headerMap, "Kennzeichen_Start")?.trim()
                val kennzeichenEnde = getCell(row, headerMap, "Kennzeichen_Ende")?.trim()

                val istStadt = getCell(row, headerMap, "Ist_Stadt")?.toBoolean() ?: false
                val istLandkreis = getCell(row, headerMap, "Ist_Landkreis")?.toBoolean() ?: false
                val istRegion = getCell(row, headerMap, "Ist_Region")?.toBoolean() ?: false
                val istKreis = getCell(row, headerMap, "Ist_Kreis")?.toBoolean() ?: false
                val istRegionKreis = getCell(row, headerMap, "Ist_Region_Kreis")?.toBoolean() ?: false

                val gesamt = parseInt(getCell(row, headerMap, "Gesamt"))
                val kfz = parseInt(getCell(row, headerMap, "Kfz"))
                val kraftrad = parseInt(getCell(row, headerMap, "Kraftrad"))
                val lastkraftwagen = parseInt(getCell(row, headerMap, "Lastkraftwagen"))
                val zugmaschine = parseInt(getCell(row, headerMap, "Zugmaschine"))
                val anhanger = parseInt(getCell(row, headerMap, "Anhänger"))
                val sonstige = parseInt(getCell(row, headerMap, "Sonstige"))

                KennzeichenEntity(
                    kuerzel = kuerzel,
                    stadt = stadt,
                    bundesland = bundesland,
                    typ = typ,
                    bevoelkerung = bevoelkerung,
                    flaeche = flaeche,
                    kennzeichenStart = kennzeichenStart,
                    kennzeichenEnde = kennzeichenEnde,
                    istStadt = istStadt,
                    istLandkreis = istLandkreis,
                    istRegion = istRegion,
                    istKreis = istKreis,
                    istRegionKreis = istRegionKreis,
                    gesamt = gesamt,
                    kfz = kfz,
                    kraftrad = kraftrad,
                    lastkraftwagen = lastkraftwagen,
                    zugmaschine = zugmaschine,
                    anhanger = anhanger,
                    sonstige = sonstige,
                    istGefunden = false
                )
            } catch (e: Exception) {
                null
            }
        }
    }

    private fun parseCsvLine(line: String): List<String> {
        // Simple CSV parsing for semicolon-separated values
        return line.split(";").map { it.trim().trim('"') }
    }

    private fun getCell(row: List<String>, headerMap: Map<String, Int>, header: String): String? {
        val index = headerMap[header]
        return if (index != null && index < row.size) row[index] else null
    }

    private fun parseInt(value: String?): Int? {
        return value?.trim()?.let { it.replace(" ", "").replace(".", "").toIntOrNull() }
    }

    private fun parseDouble(value: String?): Double? {
        return value?.trim()?.replace(",", ".")?.toDoubleOrNull()
    }
}