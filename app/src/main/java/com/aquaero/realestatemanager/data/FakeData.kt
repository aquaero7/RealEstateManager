package com.aquaero.realestatemanager.data

import android.os.Build
import androidx.annotation.RequiresApi
import androidx.compose.ui.tooling.preview.datasource.LoremIpsum
import com.aquaero.realestatemanager.model.Address
import com.aquaero.realestatemanager.model.Agent
import com.aquaero.realestatemanager.model.Photo
import com.aquaero.realestatemanager.model.Property
import java.time.LocalDate

val loremIpsum: String = LoremIpsum(300).values.first()

val fakeAgents = listOf(
    Agent(
        0,
        "N0000000",
        "F0000000"
    ),
    Agent(
        1,
        "N1111111",
        "F1111111"
    ),
    Agent(
        2,
        "N2222222",
        "F2222222"
    )
)

val fakePhotos = listOf(
    Photo(
        0,
        "U0000000",
        "L0000000"
    ),
    Photo(
        1,
        "U1111111",
        "L1111111"
    ),
    Photo(
        2,
        "U2222222",
        "L2222222"
    ),
    Photo(
        3,
        "U3333333",
        "L3333333"
    ),
    Photo(
        4,
        "U4444444",
        "L4444444"
    ),
    Photo(
        5,
        "U5555555",
        "L5555555"
    ),
    Photo(
        6,
        "U6666666",
        "L6666666"
    ),
    Photo(
        7,
        "U7777777",
        "L7777777"
    )
)

val fakeAddresses = listOf(
    Address(
        0,
        "n0000000",
        "s0000000",
        "i0000000",
        "v0000000",
        "d0000000",
        "z0000000",
        "c0000000",
        0.0,
        0.0
    ),
    Address(
        1,
        "n1111111",
        "s1111111",
        "i1111111",
        "v1111111",
        "d1111111",
        "z1111111",
        "c1111111",
        1.1,
        1.1
    ),
    Address(
        2,
        "n2222222",
        "s2222222",
        "i2222222",
        "v2222222",
        "d2222222",
        "z2222222",
        "c2222222",
        2.2,
        2.2
    )
)

@RequiresApi(Build.VERSION_CODES.O)
val fakeProperties = listOf(
    Property(
        0,
        "t0000000",
        fakeAddresses[0],
        10000000,
        "d0000000\n$loremIpsum",
        1000,
        10,
        10,
        10,
        listOf(fakePhotos[0], fakePhotos[1], fakePhotos[2], fakePhotos[3], fakePhotos[4],
            fakePhotos[5], fakePhotos[6], fakePhotos[7]),
        LocalDate.of(2023, 9, 10),  // LocalDate.parse("2023-09-10"),
        null,
        false,
        fakeAgents[0]
    ),
    Property(
        1,
        "t1111111",
        fakeAddresses[1],
        11111111,
        "d1111111\n$loremIpsum",
        1111,
        11,
        11,
        11,
        listOf(fakePhotos[0], fakePhotos[1], fakePhotos[2], fakePhotos[3], fakePhotos[4],
            fakePhotos[5], fakePhotos[6], fakePhotos[7]),
        LocalDate.of(2023, 9, 11),  // LocalDate.parse("2023-09-11"),
        null,
        false,
        fakeAgents[1]
    ),
    Property(
        2,
        "t2222222",
        fakeAddresses[2],
        12222222,
        "d2222222\n$loremIpsum",
        1222,
        12,
        12,
        12,
        listOf(fakePhotos[0], fakePhotos[1], fakePhotos[2], fakePhotos[3], fakePhotos[4],
            fakePhotos[5], fakePhotos[6], fakePhotos[7]),
        LocalDate.of(2023, 9, 12),  // LocalDate.parse("2023-09-12"),
        null,
        false,
        fakeAgents[2]
    ),
    Property(
        3,
        "t3333333",
        fakeAddresses[0],
        13333333,
        "d3333333\n$loremIpsum",
        1333,
        13,
        13,
        13,
        listOf(fakePhotos[0], fakePhotos[1], fakePhotos[2], fakePhotos[3], fakePhotos[4],
            fakePhotos[5], fakePhotos[6], fakePhotos[7]),
        LocalDate.of(2023, 9, 13),  // LocalDate.parse("2023-09-13"),
        null,
        false,
        fakeAgents[0]
    ),
    Property(
        4,
        "t4444444",
        fakeAddresses[1],
        14444444,
        "d4444444\n$loremIpsum",
        1444,
        14,
        14,
        14,
        listOf(fakePhotos[0], fakePhotos[1], fakePhotos[2], fakePhotos[3], fakePhotos[4],
            fakePhotos[5], fakePhotos[6], fakePhotos[7]),
        LocalDate.of(2023, 9, 14),  // LocalDate.parse("2023-09-14"),
        null,
        false,
        fakeAgents[1]
    ),
    Property(
        5,
        "t5555555",
        fakeAddresses[2],
        15555555,
        "d5555555\n$loremIpsum",
        1555,
        15,
        15,
        15,
        listOf(fakePhotos[0], fakePhotos[1], fakePhotos[2], fakePhotos[3], fakePhotos[4],
            fakePhotos[5], fakePhotos[6], fakePhotos[7]),
        LocalDate.of(2023, 9, 15),  // LocalDate.parse("2023-09-15"),
        null,
        false,
        fakeAgents[2]
    ),
    Property(
        6,
        "t6666666",
        fakeAddresses[0],
        16666666,
        "d6666666\n$loremIpsum",
        1666,
        16,
        16,
        16,
        listOf(fakePhotos[0], fakePhotos[1], fakePhotos[2], fakePhotos[3], fakePhotos[4],
            fakePhotos[5], fakePhotos[6], fakePhotos[7]),
        LocalDate.of(2023, 9, 16),  // LocalDate.parse("2023-09-16"),
        null,
        false,
        fakeAgents[0]
    ),
    Property(
        7,
        "t7777777",
        fakeAddresses[1],
        17777777,
        "d7777777\n$loremIpsum",
        1777,
        17,
        17,
        17,
        listOf(fakePhotos[0], fakePhotos[1], fakePhotos[2], fakePhotos[3], fakePhotos[4],
            fakePhotos[5], fakePhotos[6], fakePhotos[7]),
        LocalDate.of(2023, 9, 17),  // LocalDate.parse("2023-09-17"),
        null,
        false,
        fakeAgents[1]
    ),
    Property(
        8,
        "t8888888",
        fakeAddresses[2],
        18888888,
        "d8888888\n$loremIpsum",
        1888,
        18,
        18,
        18,
        listOf(fakePhotos[0], fakePhotos[1], fakePhotos[2], fakePhotos[3], fakePhotos[4],
            fakePhotos[5], fakePhotos[6], fakePhotos[7]),
        LocalDate.of(2023, 9, 18),  // LocalDate.parse("2023-09-18"),
        null,
        false,
        fakeAgents[2]
    ),
    Property(
        9,
        "t9999999",
        fakeAddresses[2],
        19999999,
        "d9999999\n$loremIpsum",
        1999,
        19,
        19,
        19,
        listOf(fakePhotos[0], fakePhotos[1], fakePhotos[2], fakePhotos[3], fakePhotos[4],
            fakePhotos[5], fakePhotos[6], fakePhotos[7]),
        LocalDate.of(2023, 9, 19),  // LocalDate.parse("2023-09-19"),
        null,
        false,
        fakeAgents[2]
    )
)