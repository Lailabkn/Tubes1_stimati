# Tugas Besar Strategi Algoritma 

Membuat bot untuk memenangkan permainan Galaxio

## Table of Contents

* [General Information](#general-information)
* [Kebutuhan Program](#kebutuhan-program)
* [Cara Penggunaan](#cara-penggunaan)
* [Authors](#authors)

## General Information
Dalam menyelesaikan permaianan ini, bot menggunakan algoritma greedy dengan mengutamakan makanan untuk memperbesar ukuran dan penyerangan lawan apabila ukuran bot lebih besar dari musuh atau menghindar dari lawan apabila ukuran bot lebih kecil dari musuh untuk bertahan di dalam peta dan mencapai kemenangan.

## Kebutuhan Program
Dalam menjalankan program Galaxio, dibutuhkan beberapa tools untuk menunjang kebutuhan program sebagai berikut.

1. [latest release starter pack.zip](https://github.com/EntelectChallenge/2021-Galaxio/releases/tag/2021.3.2)
2. [Java (minimal Java 11)](https://www.oracle.com/java/technologies/downloads/#java8)
3. [IntelliJ IDEA](https://www.jetbrains.com/idea/)
4. [NodeJS](https://nodejs.org/en/download/)
5. [.Net Core 3.1](https://dotnet.microsoft.com/en-us/download/dotnet/3.1)

Selain itu, diperlukan untuk membuat file `run.bat` (untuk sistem operasi Windows) yang diletakkan di dalam folder `starter-pack` agar bot dapat dijalankan dengan baik. silahkan salin isi file `run.bat` di bawah ini

```bash
@echo off
:: Game Runner
cd ./runner-publish/
start "" dotnet GameRunner.dll

:: Game Engine
cd ../engine-publish/
timeout /t 1
start "" dotnet Engine.dll

:: Game Logger
cd ../logger-publish/
timeout /t 1
start "" dotnet Logger.dll

:: Bots
cd ../reference-bot-publish/
timeout /t 3
start "" dotnet ReferenceBot.dll
timeout /t 3
start "" dotnet ReferenceBot.dll
timeout /t 3
start "" dotnet ReferenceBot.dll
timeout /t 3
start "" dotnet ReferenceBot.dll
cd ../

pause
```

## Cara Penggunaan
1. Jalankan run.bat dalam folder starter-pack
2. Tunggu hingga semua proses pada terminal berhenti
3. Buka Galaxio
4. Buka menu "Load"
5. Pilih file JSON yang ingin diload pada "GAME>LOG", lalu klik "START"
6. Setelah masuk ke visualiser, kita dapat melakukan start, pause, rewind, and reset

## Authors

* [Muhhamad Syauqi Jannatan - 13521014](https://github.com/syauqijan)
* [Laila Bilbina Khoiru Nisa - 13521016](https://github.com/Lailabkn)
* [Agsha Athalla Nurkareem - 13521027](https://github.com/agshaathalla)
