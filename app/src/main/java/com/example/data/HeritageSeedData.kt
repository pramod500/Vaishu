package com.example.data

import com.example.model.*

val initialSites: List<HeritageSite> = listOf(
    HeritageSite(
        id = "raigad-fort",
        name = "Raigad Fort",
        nativeName = "किल्ले रायगड",
        location = "Mahad, Raigad, Maharashtra",
        district = "Raigad",
        state = "Maharashtra",
        latitude = 18.2345,
        longitude = 73.4418,
        period = "Maratha Empire",
        century = "17th Century (1674 CE)",
        category = "Forts",
        monumentType = "Hill Fortress & Imperial Capital",
        shortDescription = "The legendary capital of the Maratha Empire, fortified by Chhatrapati Shivaji Maharaj, perched at 820m above sea level with formidable ramparts.",
        historyDetail = "Captured and fortified by Chhatrapati Shivaji Maharaj in 1656 from Chandrarao More, Raigad Fort was chosen as the sovereign capital of the Maratha Empire. On 6 June 1674, Shivaji Maharaj was crowned sovereign monarch (Chhatrapati) in an epoch-making coronation ceremony. The fort housed over 300 stone structures, royal palaces, markets, and strategic bastions.",
        architectureDetail = "Mastercrafted by chief architect Hiroji Indulkar. Features include the grand Rajsabha (Coronation Throne Room) engineered for pristine acoustics, the monolithic Maha Darwaza with overlapping blind corners to prevent elephant battering, the dizzying vertical sheer cliff of Hirkani Buruj, and the expansive Ganga Sagar royal water reservoir.",
        culturalImportance = "Symbolizes self-rule (Swarajya), sovereignty, and administrative brilliance. A paramount pilgrimage of pride for millions of historians, youth, and citizens.",
        importantFacts = listOf(
            "Perched at 820 meters (2,700 feet) in the Sahyadri Western Ghats mountain range.",
            "The Rajsabha acoustic design allows whispers at the entrance to be heard at the throne 50m away.",
            "Chief architect Hiroji Indulkar famously requested only an inscribed threshold stone saying 'Always in service' at Jagdishwar temple.",
            "The formidable Hirkani Buruj honors the courage of a mother, Hirkani, who climbed down the sheer cliff after gates closed."
        ),
        timelineEvents = listOf(
            TimelineEvent("1656 CE", "Fortress of Rairi seized from More dynasty by Chhatrapati Shivaji Maharaj", "Foundation"),
            TimelineEvent("1670 CE", "Comprehensive stone fortification, bastions, and royal city constructed by Hiroji Indulkar", "Expansion"),
            TimelineEvent("1674 CE", "Solemn Rajyabhisheka Coronation of Chhatrapati Shivaji Maharaj", "Capital Zenith"),
            TimelineEvent("1689 CE", "Heroic defense of Raigad by Maharani Yesubai against Mughal siege", "Resistance"),
            TimelineEvent("1818 CE", "Bombardment and capture by British East India Company cannons", "Colonial Era")
        ),
        preservationStatus = "ASI National Monument & UNESCO Tentative List",
        preservationScore = 96,
        imageUrl = "https://images.unsplash.com/photo-1627894483216-2138af692e32?auto=format&fit=crop&w=1200&q=80",
        meshType = "fortress",
        hotspots = listOf(
            Hotspot("h1", "Rajsabha (Throne Court)", "Acoustic open-air royal assembly with elevated golden throne dais.", 0.0f, 0.2f, 0.1f),
            Hotspot("h2", "Maha Darwaza (Grand Gate)", "Fortified serpentine bastion gate engineered against elephant charges.", -0.6f, -0.3f, 0.5f),
            Hotspot("h3", "Hirkani Buruj (Watch Bastion)", "Perpendicular western precipice overlooking the Konkan valleys.", 0.7f, 0.1f, -0.4f),
            Hotspot("h4", "Jagdishwar Temple", "Sacred Shiva sanctuary dedicated by Hiroji Indulkar.", 0.2f, 0.4f, -0.6f)
        ),
        audioContent = AudioContent(
            englishTranscript = "Welcome to Raigad Fort, the majestic capital of the Maratha sovereign state. Stand before the grand coronation assembly where Chhatrapati Shivaji Maharaj established swarajya in 1674. Notice the acoustics and stone masonry designed to protect the heart of freedom.",
            marathiTranscript = "किल्ले रायगडवर आपले हार्दिक स्वागत. सह्याद्रीच्या कुशीतील अजिंक्य राजधानी आणि छत्रपती शिवाजी महाराजांचे राज्याभिषेक स्थान. राजसभेच्या ध्वनीशास्त्राचे आणि भव्य महादरवाजाचे अद्वितीय स्थापत्य येथे अनुभवा.",
            hindiTranscript = "रायगढ़ किले में आपका स्वागत है। मराठा साम्राज्य की यह भव्य राजधानी जहां 1674 में छत्रपति शिवाजी महाराज का ऐतिहासिक राज्याभिषेक हुआ था। इसके अद्वितीय स्थापत्य और ऐतिहासिक महत्व को जानें।",
            durationSeconds = 210
        ),
        qrIdentifier = "HERITAGE-AR-RAIGAD-001"
    ),
    HeritageSite(
        id = "ajanta-caves",
        name = "Ajanta Caves",
        nativeName = "अजिंठा लेणी",
        location = "Aurangabad (Chhatrapati Sambhajinagar), Maharashtra",
        district = "Chhatrapati Sambhajinagar",
        state = "Maharashtra",
        latitude = 20.5519,
        longitude = 75.7033,
        period = "Satavahana & Vakataka Dynasties",
        century = "2nd Century BCE – 5th Century CE",
        category = "Caves",
        monumentType = "Monolithic Rock-Cut Buddhist Chaityas & Viharas",
        shortDescription = "World UNESCO landmark carved into a horseshoe cliff of the Waghur River gorge, renowned for priceless ancient Buddhist frescoes and stone sculptures.",
        historyDetail = "Comprising 30 rock-cut cave monuments created in two distinct phases: the early Hinayana phase under the Satavahana Empire (2nd century BCE) and the opulent Mahayana flowering under King Harishena of the Vakataka dynasty. The caves served as residential monasteries, universities, and sanctuary chaitya halls for monastic scholars along the ancient Silk & spice trade routes.",
        architectureDetail = "Excavated from Deccan volcanic basalt using simple hammer and chisel. The cave murals utilize tempera technique over clay and cow dung plaster, illuminated through strategic stone reflections. Notable caves include Cave 1 (Padmapani & Vajrapani Bodhisattvas) and Cave 26 with a colossal 7-meter reclining Buddha attaining Parinirvana.",
        culturalImportance = "Represents the pinnacle of classical ancient Indian figurative painting and Buddhist narrative art worldwide.",
        importantFacts = listOf(
            "Hidden in dense jungle until rediscovered in 1819 by British officer John Smith during a tiger hunt.",
            "Murals illustrate the Jataka tales portraying the previous exemplary lives of the Buddha.",
            "Cave 1 Bodhisattva Padmapani holding a blue lotus is an iconic global masterpiece of serene expression.",
            "Natural vegetable and mineral pigments like lapis lazuli from Afghanistan were traded to create the blues."
        ),
        timelineEvents = listOf(
            TimelineEvent("200 BCE", "Early Hinayana chaityas excavated by Satavahana patrons", "First Phase"),
            TimelineEvent("460 CE", "Mahayana renaissance under Vakataka Emperor Harishena", "Golden Art Age"),
            TimelineEvent("650 CE", "Abandonment as monk communities migrated along trade arteries", "Silent Centuries"),
            TimelineEvent("1819 CE", "Rediscovered by John Smith inscribed on Cave 10 pillar", "Rediscovery"),
            TimelineEvent("1983 CE", "Inscribed as UNESCO World Heritage Cultural Monument", "Global Heritage")
        ),
        preservationStatus = "UNESCO World Heritage Site & Protected Archaeological Reserve",
        preservationScore = 94,
        imageUrl = "https://images.unsplash.com/photo-1600100397608-f010f481b979?auto=format&fit=crop&w=1200&q=80",
        meshType = "cave",
        hotspots = listOf(
            Hotspot("aj1", "Cave 1 Bodhisattva Padmapani", "Famous fresco depicting supreme spiritual grace with a blue lotus flower.", -0.2f, 0.1f, 0.3f),
            Hotspot("aj2", "Cave 26 Parinirvana Buddha", "Colossal 7-meter monolith rock carving of Buddha's final enlightenment.", 0.5f, -0.1f, -0.2f),
            Hotspot("aj3", "Chaitya Ribbed Sun Vault", "Stone vaulted ceiling imitating curved wooden forest beams.", 0.0f, 0.6f, 0.0f)
        ),
        audioContent = AudioContent(
            englishTranscript = "Step inside the ancient Waghur gorge at Ajanta. Look upon the gentle eyes of Bodhisattva Padmapani painted over 1,500 years ago using mineral lapis lazuli pigments. Notice how the rock-hewn pillars capture sunlight to light the monastic sanctuary.",
            marathiTranscript = "अजिंठा लेण्यांच्या शांत आणि पवित्र गुंफेत आपले स्वागत. वाघूर नदीच्या कडेवर कोरलेल्या या ३० लेण्यांमधील अप्रतिम जातक कथांची चित्रे आणि भगवान बुद्धांचे महापरिनिर्वाण शिल्प प्रत्यक्ष पाहा.",
            hindiTranscript = "अजिंठा की प्राचीन गुफाओं में आपका स्वागत है। 1500 साल पुरानी भित्ति चित्रकला और बोधिसत्व पद्मपाणि की अद्वितीय करुणा को इस 3D और एआर अनुभव में निहारें।",
            durationSeconds = 195
        ),
        qrIdentifier = "HERITAGE-AR-AJANTA-002"
    ),
    HeritageSite(
        id = "ellora-caves",
        name = "Ellora Caves (Kailasa Temple)",
        nativeName = "वेरूळ लेणी (कैलास मंदिर)",
        location = "Aurangabad (Chhatrapati Sambhajinagar), Maharashtra",
        district = "Chhatrapati Sambhajinagar",
        state = "Maharashtra",
        latitude = 20.0268,
        longitude = 75.1780,
        period = "Rashtrakuta Dynasty",
        century = "8th Century CE (King Krishna I)",
        category = "Temples",
        monumentType = "Monolithic Rock-Cut Architectural Wonder",
        shortDescription = "The colossal Kailasa temple (Cave 16) is the largest single monolithic rock excavation in human history, carved top-to-bottom from a single mountain cliff.",
        historyDetail = "Over 34 rock-cut sanctuaries excavated over five centuries representing Hindu, Buddhist, and Jain harmony. Cave 16, the Kailasa Temple, was commissioned by Rashtrakuta King Krishna I around 756 CE. Craftsmen carved over 200,000 tonnes of basalt rock top-downwards from the mountain without erecting scaffolding.",
        architectureDetail = "Dravidian temple architecture carved in multi-level courtyards, elephant galleries, and a 32-meter towering shikhara vimana. Deep narrative stone reliefs depict Ravana shaking Mount Kailash, Shiva and Parvati playing dice, and epic Mahabharata friezes.",
        culturalImportance = "An unmatched global marvel of stone engineering, showcasing extraordinary devotion and mathematical architectural precision.",
        importantFacts = listOf(
            "Carved top-to-bottom from vertical rock cliff — zero margin for carving error.",
            "Over 200,000 tons of volcanic basalt were excavated using only chisels, hammers, and wooden levers.",
            "The complex covers twice the area of the Parthenon in Athens and is 1.5 times its height.",
            "Houses life-sized stone carved elephants and victory pillars (dhvajastambhas) in its grand courtyard."
        ),
        timelineEvents = listOf(
            TimelineEvent("756 CE", "Top-down excavation initiated under Rashtrakuta King Krishna I", "Construction"),
            TimelineEvent("775 CE", "Sculpting of the sacred sanctum, Nandi mandapa, and elephant friezes", "Sculpting"),
            TimelineEvent("800 CE", "Plaster and whitewashing to give the mountain-temple snowy Himalayan appearance", "Consecration"),
            TimelineEvent("1983 CE", "Designated UNESCO World Heritage Cultural Wonder", "Global Recognition")
        ),
        preservationStatus = "UNESCO World Heritage Site & Premier Archaeological Monument",
        preservationScore = 98,
        imageUrl = "https://images.unsplash.com/photo-1590050752117-238cb0fb12b1?auto=format&fit=crop&w=1200&q=80",
        meshType = "temple",
        hotspots = listOf(
            Hotspot("el1", "Ravana Shaking Mount Kailash", "Dramatic multi-armed relief depicting the demon king attempting to shake the cosmic mountain.", 0.2f, -0.2f, 0.4f),
            Hotspot("el2", "Monolithic Shikhara Spire", "32-meter high Dravidian tower carved seamlessly out of the mountain bedrock.", 0.0f, 0.7f, 0.0f),
            Hotspot("el3", "Courtyard Monolith Elephants", "Life-size pachyderms standing guard around the sanctum base.", -0.5f, -0.4f, 0.3f)
        ),
        audioContent = AudioContent(
            englishTranscript = "Gaze upon the Kailasa Temple at Ellora, the world's most astonishing monolithic achievement. Unlike buildings constructed upwards, ancient Indian masters excavated downwards from the mountain summit, creating courtyards, pillared halls, and sacred sanctums from one mountain.",
            marathiTranscript = "वेरूळच्या कैलास मंदिरात आपले स्वागत. एका सलग अखंड काळ्या पाषाणात, डोंगराच्या माथ्यापासून खाली कोरलेले जगातील सर्वात भव्य आश्चर्य. राष्ट्रकूट राजा कृष्ण प्रथम यांच्या काळातील स्थापत्य कला येथे पाहा.",
            hindiTranscript = "एलोरा के कैलास मंदिर का अवलोकन करें। एक ही विशाल चट्टान को ऊपर से नीचे तराशकर बनाया गया यह विश्व का अद्वितीय एकाश्म मंदिर है।",
            durationSeconds = 240
        ),
        qrIdentifier = "HERITAGE-AR-ELLORA-003"
    ),
    HeritageSite(
        id = "shaniwar-wada",
        name = "Shaniwar Wada",
        nativeName = "शनिवार वाडा",
        location = "Pune, Maharashtra",
        district = "Pune",
        state = "Maharashtra",
        latitude = 18.5196,
        longitude = 73.8553,
        period = "Peshwa Era, Maratha Confederacy",
        century = "18th Century (1732 CE)",
        category = "Palaces",
        monumentType = "Fortified Peshwa Palace Complex",
        shortDescription = "The historic seat of the Peshwa prime ministers of the Maratha Empire, famed for its towering Dilli Darwaza, spiked gates, and fountain gardens.",
        historyDetail = "Constructed by Peshwa Baji Rao I in 1732 as the administrative hub of the burgeoning Maratha Confederacy. It expanded into a seven-story palace of royal teakwood and stone, accommodating thousands. It witnessed pivotal decisions of Panipat and 18th-century Indian history.",
        architectureDetail = "Protected by granite curtain walls and five imposing gates: Dilli Darwaza, Mastani Darwaza, Khidki Darwaza, Ganesh Darwaza, and Jambhul Darwaza. The Dilli Darwaza features 72 steel spikes against war elephants. Inside lies the famous sixteen-petal Lotus Fountain (Hazari Karanje).",
        culturalImportance = "Epicenter of 18th-century Indian diplomacy, military strategy, and Pune's municipal heritage.",
        importantFacts = listOf(
            "Foundation stone laid on a Saturday (Shaniwar), giving it the name Shaniwar Wada.",
            "The Dilli Darwaza faces north towards Delhi, symbolizing Peshwa ambition to protect the northern borders.",
            "Hazari Karanje was a mechanical marvel with a thousand water jets creating complex floral mist patterns."
        ),
        timelineEvents = listOf(
            TimelineEvent("1730 CE", "Foundation stone laid by Peshwa Baji Rao I", "Foundation"),
            TimelineEvent("1732 CE", "Opening ceremony on Saturday with two-story timber mansion", "Inauguration"),
            TimelineEvent("1818 CE", "Union Jack hoisted as Peshwa rule concluded", "Peshwa Conclusion"),
            TimelineEvent("1828 CE", "Catastrophic seven-day fire destroyed internal wooden palaces", "Palace Fire")
        ),
        preservationStatus = "ASI Heritage Protected Monument",
        preservationScore = 90,
        imageUrl = "https://images.unsplash.com/photo-1599818816949-9b9a67a0799c?auto=format&fit=crop&w=1200&q=80",
        meshType = "fortress",
        hotspots = listOf(
            Hotspot("sw1", "Dilli Darwaza Spikes", "Steel defensive spikes angled to deter war elephants.", -0.4f, 0.1f, 0.6f),
            Hotspot("sw2", "Hazari Karanje (Fountain)", "16-petal lotus fountain engineered with gravity-fed water channels.", 0.0f, -0.3f, 0.0f),
            Hotspot("sw3", "Nagarkhana (Music Gallery)", "Elevated wooden pavilion above the entry gate for royal shehnai musicians.", 0.0f, 0.5f, 0.4f)
        ),
        audioContent = AudioContent(
            englishTranscript = "Welcome to Shaniwar Wada in Pune, the fortified palace seat of the Peshwas. Imagine the grand entrance through the iron-spiked Dilli Darwaza as royal horses and statesmen arrived to govern territories across the subcontinent.",
            marathiTranscript = "पुण्याच्या शनिवार वाड्यात आपले स्वागत. थोरले बाजीराव पेशवे यांनी १७३२ मध्ये बांधलेली ही पेशवाईची ऐतिहासिक राजधानी. अणुकुचीदार खिळ्यांचे दिल्ली दरवाजा आणि कारंजे पाहा.",
            hindiTranscript = "पुणे के ऐतिहासिक शनिवार वाड़ा में आपका स्वागत है। पेशवा बाजीराव प्रथम द्वारा निर्मित यह भव्य प्रासाद 18वीं सदी के भारत की शक्ति का केंद्र था।",
            durationSeconds = 180
        ),
        qrIdentifier = "HERITAGE-AR-SHANIWAR-004"
    ),
    HeritageSite(
        id = "gateway-of-india",
        name = "Gateway of India",
        nativeName = "गेटवे ऑफ इंडिया",
        location = "Apollo Bunder, Mumbai, Maharashtra",
        district = "Mumbai",
        state = "Maharashtra",
        latitude = 18.9220,
        longitude = 72.8347,
        period = "British Colonial Period",
        century = "20th Century (1924 CE)",
        category = "Historical Buildings",
        monumentType = "Indo-Saracenic Triumphal Arch",
        shortDescription = "Mumbai's iconic waterfront triumphal arch combining 16th-century Gujarati architecture with European design overlooking the Arabian Sea.",
        historyDetail = "Erected to commemorate the landing of King George V and Queen Mary in December 1911. Designed by Scottish architect George Wittet, it was constructed between 1915 and 1924. Historically significant as the departure point for the last British troops (Somerset Light Infantry) leaving India on 28 February 1948.",
        architectureDetail = "Built of yellow basalt stone quarried from Kharodi. Features a 26-meter high arch, perforated jali screens borrowed from Gujarat Muslim architecture, and four side turrets flanking the grand central dome.",
        culturalImportance = "The visual identity of Mumbai, symbolizing both colonial entry and India's triumphant emergence into independent sovereignty.",
        importantFacts = listOf(
            "Completed in 1924 at a cost of 2.1 million rupees.",
            "The last British regiment marched through this arch to their ships in 1948, marking the end of colonial rule.",
            "Combines Indian Gujarati tracery work with Roman triumphal arch proportions."
        ),
        timelineEvents = listOf(
            TimelineEvent("1911 CE", "King George V and Queen Mary land at Apollo Bunder", "Royal Visit"),
            TimelineEvent("1924 CE", "Inaugurated by the Viceroy, the Earl of Reading", "Opening"),
            TimelineEvent("1948 CE", "Final British troops march through, saluting Free India", "Independence Milestone")
        ),
        preservationStatus = "Grade I Heritage Landmark",
        preservationScore = 93,
        imageUrl = "https://images.unsplash.com/photo-1570168007204-dfb528c6958f?auto=format&fit=crop&w=1200&q=80",
        meshType = "arch",
        hotspots = listOf(
            Hotspot("gw1", "Central Basalt Arch", "26-meter towering central archway framing the Arabian Sea.", 0.0f, 0.3f, 0.0f),
            Hotspot("gw2", "Intricate Stone Jali", "Carved perforated sandstone latticework derived from Gujarati craftsmen.", -0.4f, 0.1f, 0.2f),
            Hotspot("gw3", "Flanking Minarets", "Octagonal corner turrets giving balanced Indo-Saracenic proportions.", 0.6f, 0.5f, -0.1f)
        ),
        audioContent = AudioContent(
            englishTranscript = "You are gazing at Mumbai's Gateway of India, facing the Arabian Sea. While conceived as a colonial welcoming arch, it ultimately witnessed history's greatest turning point as the last foreign troops departed through its arch in 1948.",
            marathiTranscript = "मुंबईच्या ऐतिहासिक गेटवे ऑफ इंडिया येथे आपले स्वागत. पिवळ्या बेसाल्ट पाषाणात बांधलेली ही २६ मीटर उंच कमान भारतीय स्वातंत्र्याची आणि ब्रिटिशांच्या अखेरच्या परतीची साक्षीदार आहे.",
            hindiTranscript = "गेटवे ऑफ इंडिया के सम्मुख आपका स्वागत है। मुंबई के तट पर स्थित यह ऐतिहासिक स्मारक 1948 में भारत से अंतिम ब्रिटिश सैनिकों के प्रस्थान का गवाह बना।",
            durationSeconds = 165
        ),
        qrIdentifier = "HERITAGE-AR-GATEWAY-005"
    ),
    HeritageSite(
        id = "konark-sun-temple",
        name = "Konark Sun Temple",
        nativeName = "କୋଣାର୍କ ସୂର୍ଯ୍ୟ ମନ୍ଦିର",
        location = "Puri District, Odisha",
        district = "Puri",
        state = "Odisha",
        latitude = 19.8876,
        longitude = 86.0945,
        period = "Eastern Ganga Dynasty",
        century = "13th Century (1250 CE)",
        category = "Temples",
        monumentType = "Cosmic Chariot Temple",
        shortDescription = "A monumental stone chariot dedicated to the Sun God Surya, with 24 intricately carved stone wheels that function as precise astronomical sundials.",
        historyDetail = "Built by King Narasimhadeva I of the Eastern Ganga Dynasty around 1250 CE. Conceived as a colossal processional chariot of Surya, pulled by seven stone horses, striding across the celestial skies.",
        architectureDetail = "Built from Khondalite stone in the Kalinga architectural style. The 24 monumental wheels with eight wide spokes and eight thin spokes function as accurate sundials, allowing time calculation down to minutes from the shadows cast on spokes.",
        culturalImportance = "A masterpiece of ancient Indian astronomical engineering, sculptural dynamism, and maritime navigational heritage.",
        importantFacts = listOf(
            "Known to European sailors as the 'Black Pagoda' due to its dark stone silhouette against the Bay of Bengal.",
            "Each stone wheel is nearly 10 feet in diameter with exquisite carved filigree detailing.",
            "The seven stone horses represent the seven days of the week and the seven solar color spectra."
        ),
        timelineEvents = listOf(
            TimelineEvent("1250 CE", "Commissioned by King Narasimhadeva I with 1,200 craftsmen", "Creation"),
            TimelineEvent("1568 CE", "Invasions damaged the main rekha deula sanctuary tower", "Disturbance"),
            TimelineEvent("1901 CE", "Jagamohana assembly hall preserved and packed with sand by archaeologists", "Preservation"),
            TimelineEvent("1984 CE", "Inscribed as UNESCO World Heritage Site", "UNESCO Status")
        ),
        preservationStatus = "UNESCO World Heritage Site",
        preservationScore = 91,
        imageUrl = "https://images.unsplash.com/photo-1628155930542-3c7a64e2c833?auto=format&fit=crop&w=1200&q=80",
        meshType = "chariot",
        hotspots = listOf(
            Hotspot("kn1", "Astronomical Sundial Wheel", "10-foot wheel whose spokes calculate precise solar time down to minutes.", -0.5f, -0.2f, 0.5f),
            Hotspot("kn2", "Jagamohana (Audience Hall)", "Pyramidal stepped tower rising with stepped terraces and dancing celestial musicians.", 0.0f, 0.4f, 0.0f),
            Hotspot("kn3", "Seven Galloping Horses", "Dynamic stone steeds carved as if surging through the coastal sands.", 0.6f, -0.4f, 0.4f)
        ),
        audioContent = AudioContent(
            englishTranscript = "Behold the Sun Temple of Konark, sculpted as the cosmic chariot of Surya. Examine the spokes of the 24 stone wheels: they are not mere decoration, but functional astronomical dials that read time by the sun's shadow.",
            marathiTranscript = "कोणार्कच्या भव्य सूर्यमंदिरात आपले स्वागत. २४ चाके आणि ७ घोड्यांचा हा भव्य सूर्य रथ म्हणजे प्राचीन भारतीय खगोलशास्त्र आणि शिल्पकलेचा सर्वोत्तम आविष्कार आहे.",
            hindiTranscript = "कोणार्क के सूर्य मंदिर का अद्भुत दृश्य। सूर्य देव के इस दिव्य रथ के 24 पहिए आज भी सूर्य की छाया से सटीक समय बताते हैं।",
            durationSeconds = 200
        ),
        qrIdentifier = "HERITAGE-AR-KONARK-006"
    ),
    HeritageSite(
        id = "sanchi-stupa",
        name = "Great Stupa at Sanchi",
        nativeName = "सांची का महान स्तूप",
        location = "Raisen, Madhya Pradesh",
        district = "Raisen",
        state = "Madhya Pradesh",
        latitude = 23.4795,
        longitude = 77.7397,
        period = "Maurya & Shunga Dynasties",
        century = "3rd Century BCE (Emperor Ashoka)",
        category = "Archaeological Sites",
        monumentType = "Ancient Buddhist Reliquary & Torana Gateways",
        shortDescription = "The oldest stone structure in India, commissioned by Emperor Ashoka the Great, featuring exquisitely carved Torana gateways illustrating non-violence and early Buddhist art.",
        historyDetail = "Emperor Ashoka constructed the nucleus of the brick stupa in the 3rd century BCE to enshrine the sacred relics of the Buddha. During the Shunga and Satavahana periods, it was encased in stone and four monumental ceremonial Torana gateways facing the cardinal directions were carved with breathtaking detail.",
        architectureDetail = "The hemispherical mound (anda) symbolizes the dome of heaven. Atop is the harmika square railing crowned by the chhatra umbrella symbolizing universal compassion. The four Torana gateways stand without mortar, joined by ingenious stone carpentry tenons.",
        culturalImportance = "Foundational monument for understanding the propagation of Dhamma, peace, and early Indian narrative sculpture.",
        importantFacts = listOf(
            "Emperor Ashoka's Lion Capital here inspired India's national emblem.",
            "The gateways were sculpted by guild workers of Vidisha, including ivory carvers whose delicate wood/ivory technique translated into stone.",
            "Inscriptions preserve names of ordinary citizens, nuns, and farmers who crowdfunded individual pillars."
        ),
        timelineEvents = listOf(
            TimelineEvent("250 BCE", "Emperor Ashoka erects brick core and Ashokan Pillar", "Origin"),
            TimelineEvent("150 BCE", "Stupa enlarged and covered in stone during Shunga period", "Expansion"),
            TimelineEvent("1st C. BCE", "Four magnificent carved Torana gateways added by Satavahana craftsmen", "Torana Gateways"),
            TimelineEvent("1989 CE", "Declared UNESCO World Heritage Landmark", "Global Preservation")
        ),
        preservationStatus = "UNESCO World Heritage Site",
        preservationScore = 97,
        imageUrl = "https://images.unsplash.com/photo-1627994353457-36e65b2f2187?auto=format&fit=crop&w=1200&q=80",
        meshType = "stupa",
        hotspots = listOf(
            Hotspot("sc1", "North Torana Gateway", "Exquisite stone lintels showing the Great Departure and animals worshipping the Bodhi tree.", 0.0f, 0.4f, 0.6f),
            Hotspot("sc2", "Hemispherical Anda", "Sacred cosmic dome enclosing the ancient relic chamber.", 0.0f, 0.1f, 0.0f),
            Hotspot("sc3", "Ashokan Pillar Inscription", "Pillar bearing Emperor Ashoka's schism edict warning against division.", -0.6f, -0.2f, 0.3f)
        ),
        audioContent = AudioContent(
            englishTranscript = "Stand before the Great Stupa of Sanchi, India's oldest preserved stone monument. Commissioned by Emperor Ashoka, its circular path encourages silent mindful meditation while the Torana gates depict lessons of unity and compassion.",
            marathiTranscript = "सांचीच्या महान स्तूपाचे दर्शन घ्या. सम्राट अशोकांनी बांधलेली ही बौद्ध धर्माची पवित्र भूमी. चारही दिशांचे तोरण दरवाजे आणि त्यावरील अप्रतिम कोरीव काम शांतता आणि करुणेचा संदेश देते.",
            hindiTranscript = "सांची के महान स्तूप में आपका स्वागत है। सम्राट अशोक द्वारा निर्मित यह भारत की प्राचीनतम पत्थर की संरचना है, जिसके तोरण द्वार विश्व शांति का संदेश देते हैं।",
            durationSeconds = 190
        ),
        qrIdentifier = "HERITAGE-AR-SANCHI-007"
    ),
    HeritageSite(
        id = "hampi",
        name = "Hampi Monuments",
        nativeName = "ಹಂಪಿಯ ಸ್ಮಾರಕಗಳು",
        location = "Vijayanagara District, Karnataka",
        district = "Vijayanagara",
        state = "Karnataka",
        latitude = 15.3350,
        longitude = 76.4600,
        period = "Vijayanagara Empire",
        century = "14th – 16th Century CE",
        category = "Archaeological Sites",
        monumentType = "Imperial Capital Ruins & Stone Chariot",
        shortDescription = "The monumental capital of the Vijayanagara Empire along the Tungabhadra river, filled with boulders, the iconic Stone Chariot, and musical stone pillars.",
        historyDetail = "Hampi was the center of the Vijayanagara Empire from 1336 to 1565. European visitors like Domingo Paes described it as one of the wealthiest cities in the world, filled with international ruby markets, river aqueducts, and colossal royal temple complexes.",
        architectureDetail = "The Vittala Temple houses the famous Stone Chariot dedicated to Garuda, whose stone wheels originally rotated. The Ranga Mandapa features 56 monolithic pillars that emit musical notes when lightly tapped (musical pillars).",
        culturalImportance = "Preserves the artistic apex of South Indian Dravidian stone sculpture and urban canal planning.",
        importantFacts = listOf(
            "Second-largest medieval city in the world in the 1500s after Beijing.",
            "The stone chariot is carved so meticulously that it mimics wooden joints and chariot pins.",
            "Featured on the Indian 50-rupee currency banknote."
        ),
        timelineEvents = listOf(
            TimelineEvent("1336 CE", "Harihara I and Bukka Raya I establish the Vijayanagara Empire", "Capital Founded"),
            TimelineEvent("1509 CE", "Golden Age under Emperor Krishnadevaraya", "Golden Zenith"),
            TimelineEvent("1565 CE", "Battle of Talikota and sacking of the imperial capital", "Abandonment"),
            TimelineEvent("1986 CE", "Inscribed as UNESCO World Heritage Site", "Preservation")
        ),
        preservationStatus = "UNESCO World Heritage Site",
        preservationScore = 95,
        imageUrl = "https://images.unsplash.com/photo-1600100397906-8c90ebc2576b?auto=format&fit=crop&w=1200&q=80",
        meshType = "chariot",
        hotspots = listOf(
            Hotspot("hp1", "Vittala Stone Chariot", "Iconic shrine sculpted as a miniature stone chariot with carved elephant guardians.", 0.0f, -0.1f, 0.4f),
            Hotspot("hp2", "Musical Pillar Hall", "56 monolithic musical pillars engineered from resonant granite acoustic clusters.", -0.4f, 0.2f, -0.2f),
            Hotspot("hp3", "Virupaksha Gopuram", "50-meter soaring temple tower that has functioned continuously since the 7th century.", 0.3f, 0.6f, 0.0f)
        ),
        audioContent = AudioContent(
            englishTranscript = "Welcome to the enchanted boulder landscape of Hampi. Stand in the courtyard of Vittala Temple where the celebrated Stone Chariot rests. Observe how each granite pillar was tuned by ancient sculptors to ring with melodic notes.",
            marathiTranscript = "विजयनगर साम्राज्याची वैभवशाली राजधानी हंपी. तुंगभद्रा नदीच्या काठी वसलेले हे ऐतिहासिक शहर, विठ्ठल मंदिरातील पाषाण रथ आणि संगीत देणारे स्तंभ येथे पाहा.",
            hindiTranscript = "हम्पी के ऐतिहासिक खंडहरों में आपका स्वागत है। विजयनगर साम्राज्य की यह राजधानी अपने अद्वितीय पाषाण रथ और संगीतमय स्तंभों के लिए विश्वविख्यात है।",
            durationSeconds = 210
        ),
        qrIdentifier = "HERITAGE-AR-HAMPI-008"
    )
)

val initialArchives: List<ArchiveItem> = listOf(
    ArchiveItem(
        id = "arc-1",
        title = "1853 British Trigonometrical Survey Map of Raigad",
        type = "Old Cartographic Map",
        yearOrEra = "1853 CE",
        description = "Detailed hand-drawn cartographic linen map recording bastion elevations, cistern depths, and secret pathways of the Maratha hill fortress.",
        location = "Maharashtra State Archives & ASI Reserve",
        preservationCondition = "High Resolution Digitized (Archival Linen)",
        archiveCode = "MAH-ARC-1853-RGD-04",
        relatedSiteId = "raigad-fort"
    ),
    ArchiveItem(
        id = "arc-2",
        title = "Brahmi Rock Inscription Charter at Cave 10",
        type = "Rock Inscription",
        yearOrEra = "2nd Century BCE",
        description = "Early Brahmi script inscription documenting donations of mountain cisterns and cave chambers by merchant guilds of ancient Sopara.",
        location = "Ajanta Cave 10 Facade",
        preservationCondition = "Protected In-Situ & Multispectral 3D Scanned",
        archiveCode = "ASI-INSCR-AJN-010-B",
        relatedSiteId = "ajanta-caves"
    ),
    ArchiveItem(
        id = "arc-3",
        title = "Copper Plate Charter of Rashtrakuta King Krishna I",
        type = "Ancient Manuscript",
        yearOrEra = "772 CE",
        description = "Sanskrit copper plate grant describing the excavation of Kailasa Temple with the famous stanza: 'When the gods saw it, they exclaimed: This is self-born, not human!'",
        location = "Archaeological Museum of Deccan Heritage",
        preservationCondition = "Preserved in Nitrogen Microclimate",
        archiveCode = "RASH-COP-ELR-772-K1",
        relatedSiteId = "ellora-caves"
    ),
    ArchiveItem(
        id = "arc-4",
        title = "Historical 19th Century Daguerreotype Photograph of Apollo Bunder",
        type = "Historical Photograph",
        yearOrEra = "1880 CE",
        description = "Rare silver gelatin print showing native sailing dhows and the waterfront before the stone foundation of the Gateway of India was laid.",
        location = "Bombay Municipal Trust Archives",
        preservationCondition = "Glass Negative Restored & Color Corrected",
        archiveCode = "BOM-PHOT-1880-APL",
        relatedSiteId = "gateway-of-india"
    ),
    ArchiveItem(
        id = "arc-5",
        title = "Oral History Powada of Hirkani's Descent",
        type = "Oral Ballad / Tradition",
        yearOrEra = "17th Century Folk Tradition",
        description = "Traditional Shahiri vocal recording narrating how a milkmaid scaled down the perpendicular cliff of Raigad to nurse her infant child.",
        location = "Oral Folk Heritage Preservation Cell",
        preservationCondition = "Digitally Remastered Audio & Translated Script",
        archiveCode = "FOLK-POW-RGD-1674",
        relatedSiteId = "raigad-fort"
    )
)

val initialSubmissions: List<CommunitySubmission> = listOf(
    CommunitySubmission(
        id = "sub-1",
        contributorName = "Dr. Kirti Sabale",
        contributorRole = "Archaeologist & Educator",
        title = "Ancient Water Cisterns of Sinhagad Fort",
        category = "Forts & Civil Engineering",
        location = "Sinhagad, Pune, Maharashtra",
        period = "12th - 17th Century",
        description = "Documented Devtaki sweet water natural rock aquifers that never dry up throughout hot summers, providing 3D scans of the rock cut filtration channels.",
        date = "Yesterday, 14:30",
        status = "Verified & Preserved"
    ),
    CommunitySubmission(
        id = "sub-2",
        contributorName = "Aditya Joshi",
        contributorRole = "Student Researcher",
        title = "Forgotten Stone Bastion at Torna Fort",
        category = "Forts",
        location = "Velhe, Pune District",
        period = "13th Century",
        description = "Photographed secondary fortification ramparts on Zunjar Machi that are currently overgrown with vegetation. Recommending drone LiDAR mapping.",
        date = "3 days ago",
        status = "Pending Review"
    ),
    CommunitySubmission(
        id = "sub-3",
        contributorName = "Priya Sen",
        contributorRole = "Heritage Enthusiast",
        title = "Folk Weaving Motifs of Paithani Silk",
        category = "Handicrafts & Traditional Arts",
        location = "Paithan, Chhatrapati Sambhajinagar",
        period = "Satavahana Dynasty Heritage",
        description = "Recorded oral memories from three master weavers preserving the Asavali flower and Peacock gold zari motifs dating back 2,000 years.",
        date = "Last week",
        status = "Approved & Archived"
    )
)

val initialQuizQuestions: List<QuizQuestion> = listOf(
    QuizQuestion(
        id = "q1",
        siteName = "Raigad Fort",
        question = "In which year did the grand Rajyabhisheka (coronation) of Chhatrapati Shivaji Maharaj take place at Raigad?",
        options = listOf("1656 CE", "1674 CE", "1680 CE", "1707 CE"),
        correctIndex = 1,
        explanation = "Chhatrapati Shivaji Maharaj was crowned sovereign monarch on 6 June 1674 at Raigad Fort, marking the formal establishment of the Maratha sovereign state (Swarajya)."
    ),
    QuizQuestion(
        id = "q2",
        siteName = "Ellora Caves",
        question = "Which extraordinary construction technique was used to sculpt the colossal Kailasa Temple (Cave 16) at Ellora?",
        options = listOf(
            "Assembled stone blocks from Rajasthan",
            "Carved top-to-bottom from a single mountain cliff",
            "Constructed using brick and limestone mortar",
            "Cast using volcanic iron frames"
        ),
        correctIndex = 1,
        explanation = "The Kailasa temple is the world's largest monolithic structure, excavated downwards from the top of the volcanic basalt cliff without scaffolding or room for error."
    ),
    QuizQuestion(
        id = "q3",
        siteName = "Konark Sun Temple",
        question = "What functional scientific purpose do the 24 stone wheels of the Konark Sun Temple serve?",
        options = listOf(
            "They are functional water wheels for the river",
            "They act as precise astronomical sundials measuring solar time",
            "They rotate to open underground treasure vaults",
            "They were decorative wheels for war chariots"
        ),
        correctIndex = 1,
        explanation = "The stone wheels at Konark feature spokes that cast shadows calculating the exact hour and minutes of the day with high precision."
    ),
    QuizQuestion(
        id = "q4",
        siteName = "Ajanta Caves",
        question = "What rare blue mineral pigment was traded along the Silk Route to paint the famous Bodhisattva Padmapani fresco in Cave 1?",
        options = listOf("Cobalt glass", "Lapis Lazuli", "Crushed turquoise", "Indigo dye"),
        correctIndex = 1,
        explanation = "Ancient artists at Ajanta imported precious Lapis Lazuli from Badakhshan (present-day Afghanistan) along the trade routes to achieve the luminous blues."
    ),
    QuizQuestion(
        id = "q5",
        siteName = "Hampi Monuments",
        question = "What remarkable acoustic phenomenon is found in the pillars of the Vittala Temple at Hampi?",
        options = listOf(
            "They amplify whispers from 1 kilometer away",
            "They produce distinct musical notes (sapta swaras) when tapped",
            "They create continuous echoing bell rings in the wind",
            "They absorb all sound causing total silence"
        ),
        correctIndex = 1,
        explanation = "The 56 monolithic musical pillars (SaReGaMa pillars) of the Vittala Temple were engineered from resonant granite to produce tonal notes when struck."
    )
)
