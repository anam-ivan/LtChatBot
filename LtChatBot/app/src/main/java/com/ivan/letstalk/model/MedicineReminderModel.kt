package com.ivan.letstalk.model

class MedicineReminderModel(medicineName: String?, noOfPills: Int?, dosage: String?) {
    private var medicineName: String
    private var noOfPills: Int = 0
    private var dosage: String

    init {
        this.medicineName = medicineName!!
        this.noOfPills = noOfPills!!
        this.dosage = dosage!!
    }

    fun getMedicineName(): String? {
        return medicineName
    }

    fun setMedicineName(name: String?) {
        medicineName = medicineName!!
    }

    fun getNoOfPills(): Int? {
        return noOfPills
    }

    fun setNoOfPills(name: String?) {
        noOfPills = noOfPills!!
    }

    fun getDosage(): String? {
        return dosage
    }

    fun setDosage(name: String?) {
        dosage = dosage!!
    }
}