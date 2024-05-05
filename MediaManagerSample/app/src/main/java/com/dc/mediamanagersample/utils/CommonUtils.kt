package com.dc.mediamanagersample.utils

import com.dc.mediamanagersample.views.CustomBottomSheet

object CommonUtils {

    val filePickerList: List<CustomBottomSheet.BottomSheetModel> =
        ArrayList<CustomBottomSheet.BottomSheetModel>().apply {
            add(CustomBottomSheet.BottomSheetModel(header = "Image"))
            add(CustomBottomSheet.BottomSheetModel(id = 1, name = "Image Picker (Single)"))
            add(CustomBottomSheet.BottomSheetModel(id = 2, name = "Image Picker (Multiple)"))

            add(CustomBottomSheet.BottomSheetModel(header = "Video"))
            add(CustomBottomSheet.BottomSheetModel(id = 3, name = "Video Picker (Single)"))
            add(CustomBottomSheet.BottomSheetModel(id = 4, name = "Video Picker (Multiple)"))

            add(CustomBottomSheet.BottomSheetModel(header = "File"))
            add(CustomBottomSheet.BottomSheetModel(id = 5, name = "File Picker (Single)"))
            add(CustomBottomSheet.BottomSheetModel(id = 511, name = "File Picker (Single All Image Types)"))
            add(CustomBottomSheet.BottomSheetModel(id = 512, name = "File Picker (Single JPG)"))
            add(CustomBottomSheet.BottomSheetModel(id = 513, name = "File Picker (Single JPEG)"))
            add(CustomBottomSheet.BottomSheetModel(id = 514, name = "File Picker (Single PNG)"))
            add(CustomBottomSheet.BottomSheetModel(id = 515, name = "File Picker (Single GIF)"))

            add(CustomBottomSheet.BottomSheetModel(id = 521, name = "File Picker (Single All Video Types)"))
            add(CustomBottomSheet.BottomSheetModel(id = 522, name = "File Picker (Single MP4)"))

            add(CustomBottomSheet.BottomSheetModel(id = 531, name = "File Picker (Single All Audio Types)"))
            add(CustomBottomSheet.BottomSheetModel(id = 532, name = "File Picker (Single MP3)"))
            add(CustomBottomSheet.BottomSheetModel(id = 533, name = "File Picker (Single M4A)"))

            add(CustomBottomSheet.BottomSheetModel(id = 541, name = "File Picker (Single All Document Types)"))
            add(CustomBottomSheet.BottomSheetModel(id = 542, name = "File Picker (Single PDF)"))
            add(CustomBottomSheet.BottomSheetModel(id = 543, name = "File Picker (Single MS_WORD)"))

            //////////////////////// File Picker (Multiple) /////////////////////////////////////////////////////

            add(CustomBottomSheet.BottomSheetModel(id = 6, name = "File Picker (Multiple)"))
            add(CustomBottomSheet.BottomSheetModel(id = 611, name = "File Picker (Multiple All Image Types)"))
            add(CustomBottomSheet.BottomSheetModel(id = 612, name = "File Picker (Multiple JPG)"))
            add(CustomBottomSheet.BottomSheetModel(id = 613, name = "File Picker (Multiple JPEG)"))
            add(CustomBottomSheet.BottomSheetModel(id = 614, name = "File Picker (Multiple PNG)"))
            add(CustomBottomSheet.BottomSheetModel(id = 615, name = "File Picker (Multiple GIF)"))

            add(CustomBottomSheet.BottomSheetModel(id = 621, name = "File Picker (Multiple All Video Types)"))
            add(CustomBottomSheet.BottomSheetModel(id = 622, name = "File Picker (Multiple MP4)"))

            add(CustomBottomSheet.BottomSheetModel(id = 631, name = "File Picker (Multiple All Audio Types)"))
            add(CustomBottomSheet.BottomSheetModel(id = 632, name = "File Picker (Multiple MP3)"))
            add(CustomBottomSheet.BottomSheetModel(id = 633, name = "File Picker (Multiple M4A)"))

            add(CustomBottomSheet.BottomSheetModel(id = 641, name = "File Picker (Multiple All Document Types)"))
            add(CustomBottomSheet.BottomSheetModel(id = 642, name = "File Picker (Multiple PDF)"))
            add(CustomBottomSheet.BottomSheetModel(id = 643, name = "File Picker (Multiple MS_WORD)"))
        }
}