package com.example.util

data class Municipality(
    val nameAr: String,
    val nameFr: String,
    val latitude: Double,
    val longitude: Double,
    val postalCode: String = ""
)

data class Wilaya(
    val code: String,
    val nameAr: String,
    val nameFr: String,
    val latitude: Double,
    val longitude: Double,
    val municipalities: List<Municipality>
)

object AlgeriaLocations {

    val ALGIERS_DEFAULT = Coordinates(36.7441, 3.0428, "ولاية الجزائر، بلدية حيدرة")

    val wilayas: List<Wilaya> = listOf(
        Wilaya(
            code = "16",
            nameAr = "16 - الجزائر العاصمة",
            nameFr = "Alger",
            latitude = 36.7538,
            longitude = 3.0588,
            municipalities = listOf(
                Municipality("حيدرة", "Hydra", 36.7441, 3.0428, "16035"),
                Municipality("سيدي امحمد (الجزائر الوسطى)", "Sidi M'Hamed", 36.7642, 3.0543, "16000"),
                Municipality("باب الوادي", "Bab El Oued", 36.7903, 3.0540, "16008"),
                Municipality("دالي ابراهيم", "Dely Ibrahim", 36.7511, 2.9856, "16020"),
                Municipality("الأبيار", "El Biar", 36.7681, 3.0298, "16030"),
                Municipality("بئر مراد رايس", "Bir Mourad Raïs", 36.7333, 3.0500, "16015"),
                Municipality("بن عكنون", "Ben Aknoun", 36.7600, 3.0167, "16028"),
                Municipality("القبة", "Kouba", 36.7267, 3.0833, "16050"),
                Municipality("بوزريعة", "Bouzareah", 36.7917, 3.0167, "16032"),
                Municipality("الشراقة", "Chéraga", 36.7667, 2.9500, "16014"),
                Municipality("زرالدة", "Zeralda", 36.7167, 2.8500, "16063"),
                Municipality("برج الكيفان", "Bordj El Kiffan", 36.7500, 3.1833, "16110"),
                Municipality("الدار البيضاء", "Dar El Beïda", 36.7133, 3.2125, "16100"),
                Municipality("الرويبة", "Rouïba", 36.7333, 3.2833, "16017"),
                Municipality("بئر خادم", "Birkhadem", 36.7167, 3.0500, "16029"),
                Municipality("العاشور", "El Achour", 36.7389, 3.0039, "16104"),
                Municipality("عين البنيان", "Aïn Benian", 36.8019, 2.9219, "16018"),
                Municipality("الحراش", "El Harrach", 36.7200, 3.1333, "16200"),
                Municipality("درارية", "Draria", 36.7167, 3.0000, "16075"),
                Municipality("براقي", "Baraki", 36.6667, 3.0833, "16210"),
                Municipality("بولوغين", "Bologhine", 36.8000, 3.0500, "16011")
            )
        ),
        Wilaya(
            code = "31",
            nameAr = "31 - وهران",
            nameFr = "Oran",
            latitude = 35.6987,
            longitude = -0.6349,
            municipalities = listOf(
                Municipality("وهران (الوسط)", "Oran Centre", 35.6987, -0.6349, "31000"),
                Municipality("بئر الجير", "Bir El Djir", 35.7167, -0.5500, "31130"),
                Municipality("السانية", "Es Senia", 35.6500, -0.6167, "31100"),
                Municipality("عين الترك", "Aïn El Turk", 35.7442, -0.7500, "31200"),
                Municipality("أرزيو", "Arzew", 35.8500, -0.3167, "31230"),
                Municipality("قديل", "Gdyel", 35.7833, -0.4333, "31240"),
                Municipality("بطيوة", "Bethioua", 35.8000, -0.2667, "31210"),
                Municipality("بوتليليس", "Boutlelis", 35.5667, -0.9000, "31110"),
                Municipality("مرسى الحجاج", "Marsat El Hadjadj", 35.7833, -0.1667, "31270")
            )
        ),
        Wilaya(
            code = "25",
            nameAr = "25 - قسنطينة",
            nameFr = "Constantine",
            latitude = 36.3650,
            longitude = 6.6147,
            municipalities = listOf(
                Municipality("قسنطينة (وسط المدينة)", "Constantine Centre", 36.3650, 6.6147, "25000"),
                Municipality("الخروب", "El Khroub", 36.2628, 6.6942, "25100"),
                Municipality("علي منجلي (المدينة الجديدة)", "Ali Mendjeli", 36.2417, 6.5750, "25112"),
                Municipality("حامة بوزيان", "Hamma Bouziane", 36.4167, 6.6000, "25200"),
                Municipality("عين السمارة", "Aïn Smara", 36.2667, 6.5000, "25140"),
                Municipality("ديدوش مراد", "Didouche Mourad", 36.4500, 6.6333, "25210"),
                Municipality("زيغود يوسف", "Zighoud Youcef", 36.5333, 6.7167, "25220")
            )
        ),
        Wilaya(
            code = "09",
            nameAr = "09 - البليدة",
            nameFr = "Blida",
            latitude = 36.4700,
            longitude = 2.8300,
            municipalities = listOf(
                Municipality("البليدة (الوسط)", "Blida Centre", 36.4700, 2.8300, "09000"),
                Municipality("بوفاريك", "Boufarik", 36.5700, 2.9100, "09400"),
                Municipality("أولاد يعيش", "Ouled Yaïch", 36.5000, 2.8700, "09008"),
                Municipality("وادي العلايق", "Oued Alleug", 36.5500, 2.7800, "09015"),
                Municipality("العفرون", "El Affroun", 36.4667, 2.6333, "09300"),
                Municipality("موزاية", "Mouzaïa", 36.4667, 2.6833, "09200"),
                Municipality("بني مراد", "Beni Mered", 36.5167, 2.8667, "09018"),
                Municipality("الشبلي", "Chebli", 36.5833, 3.0000, "09410")
            )
        ),
        Wilaya(
            code = "19",
            nameAr = "19 - سطيف",
            nameFr = "Sétif",
            latitude = 36.1900,
            longitude = 5.4100,
            municipalities = listOf(
                Municipality("سطيف (وسط المدينة)", "Sétif Centre", 36.1900, 5.4100, "19000"),
                Municipality("العلمة", "El Eulma", 36.1500, 5.6800, "19600"),
                Municipality("عين ولمان", "Aïn Oulmene", 35.9167, 5.3000, "19200"),
                Municipality("عين الكبيرة", "Aïn El Kebira", 36.3667, 5.5000, "19400"),
                Municipality("قجال", "Guedjel", 36.1167, 5.5167, "19014"),
                Municipality("بوقاعة", "Bougaa", 36.3333, 5.0833, "19300"),
                Municipality("صالح باي", "Salah Bey", 35.8500, 5.2833, "19210")
            )
        ),
        Wilaya(
            code = "23",
            nameAr = "23 - عنابة",
            nameFr = "Annaba",
            latitude = 36.9000,
            longitude = 7.7667,
            municipalities = listOf(
                Municipality("عنابة (الوسط)", "Annaba Centre", 36.9000, 7.7667, "23000"),
                Municipality("البوني", "El Bouni", 36.8500, 7.7333, "23100"),
                Municipality("سيدي عمار", "Sidi Amar", 36.8000, 7.7000, "23200"),
                Municipality("واد العنب", "Oued El Aneb", 36.9167, 7.5000, "23300"),
                Municipality("شطايبي", "Chetaïbi", 37.0333, 7.3667, "23010"),
                Municipality("برحال", "Berrahal", 36.8333, 7.4500, "23400")
            )
        ),
        Wilaya(
            code = "15",
            nameAr = "15 - تيزي وزو",
            nameFr = "Tizi Ouzou",
            latitude = 36.7167,
            longitude = 4.0500,
            municipalities = listOf(
                Municipality("تيزي وزو (الوسط)", "Tizi Ouzou Centre", 36.7167, 4.0500, "15000"),
                Municipality("دراع بن خدة", "Draa Ben Khedda", 36.7333, 3.9667, "15100"),
                Municipality("أزفون", "Azeffoun", 36.8964, 4.4217, "15400"),
                Municipality("تيقزيرت", "Tigzirt", 36.8833, 4.1167, "15600"),
                Municipality("عزازقة", "Azazga", 36.7500, 4.3667, "15300"),
                Municipality("الأربعاء نايث إيراثن", "Larbaa Nath Irathen", 36.6333, 4.2000, "15200"),
                Municipality("بوغني", "Boghni", 36.5333, 3.9667, "15450")
            )
        ),
        Wilaya(
            code = "06",
            nameAr = "06 - بجاية",
            nameFr = "Béjaïa",
            latitude = 36.7500,
            longitude = 5.0667,
            municipalities = listOf(
                Municipality("بجاية (الوسط والميناء)", "Béjaïa Centre", 36.7500, 5.0667, "06000"),
                Municipality("أقبو", "Akbou", 36.4500, 4.5333, "06200"),
                Municipality("أميزور", "Amizour", 36.6500, 4.9000, "06300"),
                Municipality("القصر", "El Kseur", 36.6833, 4.8500, "06100"),
                Municipality("تيشي", "Tichy", 36.6667, 5.1667, "06005"),
                Municipality("أوقاس", "Aokas", 36.6333, 5.2333, "06006"),
                Municipality("سيدي عيش", "Sidi Aïch", 36.6167, 4.6833, "06400")
            )
        ),
        Wilaya(
            code = "13",
            nameAr = "13 - تلمسان",
            nameFr = "Tlemcen",
            latitude = 34.8828,
            longitude = -1.3167,
            municipalities = listOf(
                Municipality("تلمسان (الوسط)", "Tlemcen Centre", 34.8828, -1.3167, "13000"),
                Municipality("منصورة", "Mansourah", 34.8667, -1.3333, "13100"),
                Municipality("مغنية", "Maghnia", 34.8500, -1.7333, "13300"),
                Municipality("شتوان", "Chetouane", 34.9167, -1.2833, "13005"),
                Municipality("سبدو", "Sebdou", 34.6333, -1.3333, "13200"),
                Municipality("ندرومة", "Nedroma", 35.0167, -1.7500, "13600"),
                Municipality("الغزوات", "Ghazaouet", 35.1000, -1.8500, "13500")
            )
        ),
        Wilaya(
            code = "05",
            nameAr = "05 - باتنة",
            nameFr = "Batna",
            latitude = 35.5559,
            longitude = 6.1741,
            municipalities = listOf(
                Municipality("باتنة (الوسط)", "Batna Centre", 35.5559, 6.1741, "05000"),
                Municipality("بريكة", "Barika", 35.3897, 5.3658, "05200"),
                Municipality("عين التوتة", "Aïn Touta", 35.3833, 5.9000, "05500"),
                Municipality("مروانة", "Merouana", 35.6333, 5.9167, "05300"),
                Municipality("نقاوس", "N'Gaous", 35.5667, 5.6167, "05400"),
                Municipality("أريس", "Arris", 35.2667, 6.3500, "05100"),
                Municipality("تازولت", "Tazoult", 35.4833, 6.2667, "05008")
            )
        ),
        Wilaya(
            code = "35",
            nameAr = "35 - بومرداس",
            nameFr = "Boumerdès",
            latitude = 36.7667,
            longitude = 3.4833,
            municipalities = listOf(
                Municipality("بومرداس (الواجهة البحرية)", "Boumerdès Ville", 36.7667, 3.4833, "35000"),
                Municipality("برج منايل", "Bordj Menaïel", 36.7431, 3.7192, "35200"),
                Municipality("بودواو", "Boudouaou", 36.7297, 3.4097, "35100"),
                Municipality("دلس", "Dellys", 36.9167, 3.9167, "35300"),
                Municipality("خميس الخشنة", "Khemis El Khechna", 36.6500, 3.3333, "35400"),
                Municipality("يسر", "Isser", 36.7167, 3.6667, "35250"),
                Municipality("الثنية", "Thenia", 36.7250, 3.5567, "35005")
            )
        ),
        Wilaya(
            code = "42",
            nameAr = "42 - تيبازة",
            nameFr = "Tipaza",
            latitude = 36.5936,
            longitude = 2.4439,
            municipalities = listOf(
                Municipality("تيبازة (الآثار الرومانية)", "Tipaza Ville", 36.5936, 2.4439, "42000"),
                Municipality("القليعة", "Koléa", 36.6392, 2.7669, "42400"),
                Municipality("بوسماعيل", "Bou Ismaïl", 36.6433, 2.6917, "42100"),
                Municipality("فوكة", "Fouka", 36.6667, 2.7500, "42200"),
                Municipality("شرشال", "Cherchell", 36.6083, 2.1931, "42110"),
                Municipality("حجوط", "Hadjout", 36.5167, 2.4167, "42300")
            )
        ),
        Wilaya(
            code = "02",
            nameAr = "02 - الشلف",
            nameFr = "Chlef",
            latitude = 36.1647,
            longitude = 1.3317,
            municipalities = listOf(
                Municipality("الشلف (الوسط)", "Chlef Centre", 36.1647, 1.3317, "02000"),
                Municipality("تنس", "Ténès", 36.5125, 1.3061, "02200"),
                Municipality("وادي الفضة", "Oued Fodda", 36.1833, 1.5333, "02100"),
                Municipality("بوقادير", "Boukadir", 36.0667, 1.1333, "02300")
            )
        ),
        Wilaya(
            code = "10",
            nameAr = "10 - البويرة",
            nameFr = "Bouira",
            latitude = 36.3750,
            longitude = 3.9000,
            municipalities = listOf(
                Municipality("البويرة (الوسط)", "Bouira Centre", 36.3750, 3.9000, "10000"),
                Municipality("الأخضرية", "Lakhdaria", 36.5667, 3.5833, "10200"),
                Municipality("سور الغزلان", "Sour El Ghozlane", 36.1500, 3.6833, "10300"),
                Municipality("عين بسام", "Aïn Bessem", 36.3000, 3.6667, "10400"),
                Municipality("مشدالة", "M'Chedallah", 36.3667, 4.2833, "10500")
            )
        ),
        Wilaya(
            code = "17",
            nameAr = "17 - الجلفة",
            nameFr = "Djelfa",
            latitude = 34.6728,
            longitude = 3.2630,
            municipalities = listOf(
                Municipality("الجلفة (الوسط)", "Djelfa Centre", 34.6728, 3.2630, "17000"),
                Municipality("عين وسارة", "Aïn Oussara", 35.4500, 2.9167, "17200"),
                Municipality("مسعد", "Messaad", 34.1667, 3.5000, "17300"),
                Municipality("حاسي بحبح", "Hassi Bahbah", 35.0833, 3.0333, "17100")
            )
        ),
        Wilaya(
            code = "27",
            nameAr = "27 - مستغانم",
            nameFr = "Mostaganem",
            latitude = 35.9333,
            longitude = 0.0900,
            municipalities = listOf(
                Municipality("مستغانم (الوسط)", "Mostaganem Centre", 35.9333, 0.0900, "27000"),
                Municipality("سيدي علي", "Sidi Ali", 36.0833, 0.4167, "27200"),
                Municipality("عين تادلس", "Aïn Tedeles", 35.9833, 0.3000, "27100"),
                Municipality("حاسي مماش", "Hassi Mameche", 35.8667, 0.0667, "27005"),
                Municipality("مزغران", "Mazagran", 35.9000, 0.0667, "27010")
            )
        ),
        Wilaya(
            code = "30",
            nameAr = "30 - ورقلة",
            nameFr = "Ouargla",
            latitude = 31.9500,
            longitude = 5.3333,
            municipalities = listOf(
                Municipality("ورقلة (الوسط)", "Ouargla Centre", 31.9500, 5.3333, "30000"),
                Municipality("حاسي مسعود", "Hassi Messaoud", 31.6806, 6.0728, "30500"),
                Municipality("الرويسات", "Rouissat", 31.9167, 5.3500, "30002"),
                Municipality("سيدي خويلد", "Sidi Khouiled", 32.0000, 5.4167, "30200")
            )
        ),
        Wilaya(
            code = "47",
            nameAr = "47 - غرداية",
            nameFr = "Ghardaïa",
            latitude = 32.4900,
            longitude = 3.6700,
            municipalities = listOf(
                Municipality("غرداية (وادي ميزاب)", "Ghardaïa Centre", 32.4900, 3.6700, "47000"),
                Municipality("القرارة", "El Guerrara", 32.7833, 4.5000, "47100"),
                Municipality("متليلي", "Metlili", 32.2667, 3.6333, "47200"),
                Municipality("بريان", "Berriane", 32.8333, 3.7667, "47300"),
                Municipality("بنورة", "Bounoura", 32.4833, 3.7000, "47008")
            )
        ),
        Wilaya(
            code = "07",
            nameAr = "07 - بسكرة",
            nameFr = "Biskra",
            latitude = 34.8500,
            longitude = 5.7333,
            municipalities = listOf(
                Municipality("بسكرة (عروس الزيبان)", "Biskra Centre", 34.8500, 5.7333, "07000"),
                Municipality("طولقة", "Tolga", 34.7167, 5.3833, "07100"),
                Municipality("سيدي عقبة", "Sidi Okba", 34.7500, 5.9000, "07200")
            )
        ),
        Wilaya(
            code = "14",
            nameAr = "14 - تيارت",
            nameFr = "Tiaret",
            latitude = 35.3711,
            longitude = 1.3169,
            municipalities = listOf(
                Municipality("تيارت (الوسط)", "Tiaret Centre", 35.3711, 1.3169, "14000"),
                Municipality("فرندة", "Frenda", 35.0667, 1.0500, "14200"),
                Municipality("السوقر", "Sougueur", 35.1833, 1.5000, "14100")
            )
        ),
        Wilaya(
            code = "21",
            nameAr = "21 - سكيكدة",
            nameFr = "Skikda",
            latitude = 36.8786,
            longitude = 6.9061,
            municipalities = listOf(
                Municipality("سكيكدة (روسيكادا)", "Skikda Centre", 36.8786, 6.9061, "21000"),
                Municipality("الحروش", "El Harrouch", 36.6500, 6.8333, "21200"),
                Municipality("القل", "Collo", 37.0000, 6.5667, "21100"),
                Municipality("عزابة", "Azzaba", 36.7500, 7.1000, "21300")
            )
        ),
        Wilaya(
            code = "34",
            nameAr = "34 - برج بوعريريج",
            nameFr = "Bordj Bou Arreridj",
            latitude = 36.0732,
            longitude = 4.7611,
            municipalities = listOf(
                Municipality("برج بوعريريج (عاصمة الإلكترونيك)", "BBA Centre", 36.0732, 4.7611, "34000"),
                Municipality("رأس الوادي", "Ras El Oued", 35.9500, 5.0333, "34200"),
                Municipality("منصورة", "Mansoura", 36.0833, 4.4500, "34100")
            )
        ),
        Wilaya(
            code = "43",
            nameAr = "43 - ميلة",
            nameFr = "Mila",
            latitude = 36.4503,
            longitude = 6.2644,
            municipalities = listOf(
                Municipality("ميلة (الوسط)", "Mila Centre", 36.4503, 6.2644, "43000"),
                Municipality("شلغوم العيد", "Chelghoum Laïd", 36.1667, 6.1667, "43200"),
                Municipality("فرجيوة", "Ferdjioua", 36.4000, 5.9667, "43100"),
                Municipality("تاجنانت", "Tadjenanet", 36.1167, 5.9833, "43300")
            )
        ),
        Wilaya(
            code = "44",
            nameAr = "44 - عين الدفلى",
            nameFr = "Aïn Defla",
            latitude = 36.2644,
            longitude = 1.9678,
            municipalities = listOf(
                Municipality("عين الدفلى (الوسط)", "Aïn Defla Centre", 36.2644, 1.9678, "44000"),
                Municipality("خميس مليانة", "Khemis Miliana", 36.2611, 2.2217, "44200"),
                Municipality("مليانة", "Miliana", 36.3000, 2.2333, "44100"),
                Municipality("العطاف", "El Attaf", 36.2167, 1.6667, "44300")
            )
        ),
        Wilaya(
            code = "26",
            nameAr = "26 - المدية",
            nameFr = "Médéa",
            latitude = 36.2675,
            longitude = 2.7500,
            municipalities = listOf(
                Municipality("المدية (الوسط)", "Médéa Centre", 36.2675, 2.7500, "26000"),
                Municipality("البرواقية", "Berrouaghia", 36.1333, 2.9167, "26200"),
                Municipality("قصر البخاري", "Ksar El Boukhari", 35.8833, 2.7500, "26300"),
                Municipality("بني سليمان", "Beni Slimane", 36.2167, 3.3000, "26100")
            )
        ),
        Wilaya(
            code = "01",
            nameAr = "01 - أدرار",
            nameFr = "Adrar",
            latitude = 27.8742,
            longitude = -0.2939,
            municipalities = listOf(
                Municipality("أدرار (الوسط)", "Adrar Centre", 27.8742, -0.2939, "01000"),
                Municipality("تيميمون", "Timimoun", 29.2639, 0.2311, "01400"),
                Municipality("رقان", "Reggane", 26.7167, 0.1667, "01200")
            )
        ),
        Wilaya(
            code = "11",
            nameAr = "11 - تمنراست",
            nameFr = "Tamanrasset",
            latitude = 22.7850,
            longitude = 5.5228,
            municipalities = listOf(
                Municipality("تمنراست (الوسط)", "Tamanrasset Centre", 22.7850, 5.5228, "11000"),
                Municipality("عين قزام", "In Guezzam", 19.5667, 5.7667, "11200"),
                Municipality("عين صالح", "In Salah", 27.1939, 2.4828, "11100")
            )
        )
    )

    fun getWilaya(code: String): Wilaya? {
        return wilayas.find { it.code == code }
    }

    fun getMunicipalities(wilayaCode: String): List<Municipality> {
        return wilayas.find { it.code == wilayaCode }?.municipalities ?: emptyList()
    }

    fun defaultWilaya(): Wilaya = wilayas.first() // 16 - Alger
    fun defaultMunicipality(): Municipality = wilayas.first().municipalities.first() // Hydra
}
