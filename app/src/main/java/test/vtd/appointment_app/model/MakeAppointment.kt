package test.vtd.appointment_app.model

data class MakeAppointment (
    var id:Int = 0,
    var doctorId:Int = 0,
    var userId:Int = 0,
    var time:String = " ",
    var note:String = " ",
    var state:Int = 0
)