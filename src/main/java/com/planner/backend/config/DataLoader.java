package com.planner.backend.config;

import com.planner.backend.model.Comment;
import com.planner.backend.model.Job;
import com.planner.backend.model.Offer;
import com.planner.backend.model.PartnerOffer;
import com.planner.backend.model.User;
import com.planner.backend.repository.CommentRepository;
import com.planner.backend.repository.JobRepository;
import com.planner.backend.repository.OfferRepository;
import com.planner.backend.repository.PartnerOfferRepository;
import com.planner.backend.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.stereotype.Component;

import java.time.LocalDateTime;

@Component
public class DataLoader implements CommandLineRunner {

    @Autowired private UserRepository userRepository;
    @Autowired private JobRepository jobRepository;
    @Autowired private OfferRepository offerRepository;
    @Autowired private CommentRepository commentRepository;
    @Autowired private PartnerOfferRepository partnerOfferRepository;

    @Override
    public void run(String... args) throws Exception {
        if (userRepository.count() == 0) {
            // ---- ADMIN OLUŞTUR ----
            userRepository.save(User.builder()
                    .username("admin").password("admin")
                    .fullName("Sistem Yöneticisi")
                    .role("ROLE_ADMIN")
                    .isPaid(true)
                    .subscriptionType("PRO_ENTITY")
                    .subscriptionExpiryDate(LocalDateTime.now().plusYears(1))
                    .build());

            // ---- PLANCILARI OLUŞTUR ----
            User planner1 = userRepository.save(User.builder()
                    .username("planner1").password("pass")
                    .fullName("Ahmet Yılmaz")
                    .role("ROLE_PLANNER")
                    .karne("A")
                    .bio("15 yıllık şehir plancılığı deneyimine sahip, özellikle kentsel dönüşüm ve imar planlaması alanlarında uzmanlaşmış bir profesyonelim.")
                    .skills("Kentsel Dönüşüm, İmar Planlaması, CBS, Çevre Planlama, 3D Modelleme")
                    .location("İstanbul, Türkiye")
                    .phone("+90 532 111 22 33")
                    .completedWorks("• 2023 – Kartal Sahil Kentsel Dönüşüm Projesi (8.500 konut, İstanbul)\n• 2022 – Başakşehir Yeni Mahalle İmar Planı Revizyonu (12.000 dönüm)\n• 2021 – Kadıköy Tarihi Çarşı Koruma Alanı Planlaması\n• 2020 – Ataşehir TOKİ Konut Alanı Master Planı")
                    .isPaid(true)
                    .subscriptionType("PREMIUM_PLANNER")
                    .subscriptionExpiryDate(LocalDateTime.now().plusYears(1))
                    .build());

            User planner2 = userRepository.save(User.builder()
                    .username("planner2").password("pass")
                    .fullName("Ayşe Kaya")
                    .role("ROLE_PLANNER")
                    .karne("B")
                    .bio("Şehir ve bölge planlama alanında doktora derecesine sahip, sürdürülebilir kentleşme ve yeşil altyapı konularında uzman bir akademisyen-pratisyenim.")
                    .skills("Sürdürülebilir Kentleşme, Yeşil Altyapı, Ulaşım Planlaması, Coğrafi Analiz")
                    .location("Ankara, Türkiye")
                    .phone("+90 533 222 44 55")
                    .completedWorks("• 2024 – Ankara Çankaya Bisiklet Yolu Entegrasyon Projesi\n• 2023 – Keçiören Yeşil Koridor ve Park Ağı Planlaması\n• 2022 – Mamak Bölgesi Kentsel Yenileme Master Planı")
                    .isPaid(true)
                    .subscriptionType("PREMIUM_PLANNER")
                    .subscriptionExpiryDate(LocalDateTime.now().plusMonths(6))
                    .build());

            User planner3 = userRepository.save(User.builder()
                    .username("planner3").password("pass")
                    .fullName("Mehmet Demir")
                    .role("ROLE_PLANNER")
                    .karne("C")
                    .bio("Yeni mezun şehir plancısı. GİS araçları ve kentsel tasarım yazılımlarına hakimim. Deneyim kazanmak için projelerde görev almayı hedefliyorum.")
                    .skills("ArcGIS, AutoCAD, SketchUp, Photoshop")
                    .location("İzmir, Türkiye")
                    .phone("+90 555 333 11 22")
                    .isPaid(false)
                    .subscriptionType("FREE_PLANNER")
                    .build());

            // ---- KURUMLARI OLUŞTUR ----
            User entity1 = userRepository.save(User.builder()
                    .username("entity1").password("pass")
                    .fullName("ABC İnşaat ve Yapı A.Ş.")
                    .role("ROLE_ENTITY")
                    .entityType("TUZEL")
                    .bio("1998 yılında kurulan firmamız, Türkiye genelinde 200'den fazla projeyi başarıyla tamamlamış, sektörün önde gelen inşaat ve gayrimenkul geliştirme şirketlerinden biridir.")
                    .skills("Konut Geliştirme, Kentsel Dönüşüm, Ticari Yapılar, Altyapı")
                    .location("İstanbul, Türkiye")
                    .phone("+90 212 555 66 77")
                    .completedWorks("• Kartal Marina Rezidans (1.200 konut)\n• Ataşehir AVM ve Ofis Kompleksi\n• Başakşehir Toplu Konut Projesi (3.500 konut)")
                    .isPaid(true)
                    .subscriptionType("PRO_ENTITY")
                    .subscriptionExpiryDate(LocalDateTime.now().plusYears(1))
                    .build());

            User entity2 = userRepository.save(User.builder()
                    .username("entity2").password("pass")
                    .fullName("Belediye İmar ve Geliştirme A.Ş.")
                    .role("ROLE_ENTITY")
                    .entityType("KAMU")
                    .bio("Kamu kurumlarına bağlı bir proje geliştirme şirketi olarak, şehrin geleceğini şekillendiren büyük ölçekli kentsel dönüşüm ve altyapı projelerini hayata geçiriyoruz.")
                    .skills("Kentsel Altyapı, Belediye Projeleri, Dönüşüm, Ulaşım")
                    .location("Ankara, Türkiye")
                    .phone("+90 312 444 55 66")
                    .isPaid(false)
                    .subscriptionType("FREE_ENTITY")
                    .build());

            // ---- İŞ (JOB) OLUŞTUR ----
            Job job1 = jobRepository.save(Job.builder()
                    .title("Kartal İlçesi Kuzey Bölgesi İmar Planı Revizyonu")
                    .description("İstanbul Kartal ilçesinin kuzey bölgesinde yer alan yaklaşık 5.000 dönümlük alanın 1/5000 ölçekli nazım imar planı ve 1/1000 ölçekli uygulama imar planı hazırlanması işi.")
                    .jobType("PLANLAMA")
                    .minKarne("B")
                    .status("OPEN")
                    .creator(entity1)
                    .isNazimImarPlani(true)
                    .isUygulamaImarPlani(true)
                    .hasZeminEtudu(true)
                    .hasHalihazirHarita(true)
                    .hasKurumGorusleri(false)
                    .isParselasyon(false)
                    .areaSize(500.0)
                    .locationDetails("İstanbul / Kartal / Uğur Mumcu")
                    .build());
                    
            Job job2 = jobRepository.save(Job.builder()
                    .title("Sürdürülebilir Yaya Bölgesi Planlaması – Çankaya Merkez")
                    .description("Ankara Çankaya ilçesi merkez alanında yaya bölgesine dönüştürülmesi için gerekli planlama çalışması.")
                    .jobType("DANIŞMANLIK")
                    .detailedInfo("Ada/Parsel: 1234/56")
                    .priceRangeMin(150000.0)
                    .priceRangeMax(250000.0)
                    .minKarne("C")
                    .status("OPEN")
                    .creator(entity2)
                    .locationDetails("Ankara / Çankaya")
                    .build());

            Job job3 = jobRepository.save(Job.builder()
                    .title("Yeşil Altyapı ve Parklar Ağı Master Planı")
                    .description("İlçe genelinde toplam 12 parkın birbirine bağlandığı yeşil koridor sisteminin planlanması.")
                    .jobType("PLANLAMA")
                    .status("OPEN")
                    .creator(entity1)
                    .isUygulamaImarPlani(true)
                    .hasZeminEtudu(false)
                    .hasHalihazirHarita(true)
                    .isParselasyon(true)
                    .areaSize(120.5)
                    .locationDetails("İzmir / Bornova")
                    .build());

            Job job4 = jobRepository.save(Job.builder()
                    .title("Kentsel Yenileme ve Ulaşım Entegrasyon Planı")
                    .description("Mevcut banliyö hattı istasyon çevresinin yeniden planlanması.")
                    .jobType("DANIŞMANLIK")
                    .detailedInfo("Ada/Parsel: 987/65")
                    .priceRangeMin(200000.0)
                    .priceRangeMax(300000.0)
                    .status("OPEN")
                    .creator(entity2)
                    .locationDetails("Bursa / Nilüfer")
                    .build());

            Job job5 = jobRepository.save(Job.builder()
                    .title("Yeni Konut Gelişim Alanı Master Planı")
                    .description("10.000 nüfuslu yeni bir konut gelişim alanı için altyapı, yeşil alan ve konut yerleşim master planının oluşturulması.")
                    .jobType("PLANLAMA")
                    .status("OPEN")
                    .creator(entity1)
                    .isNazimImarPlani(true)
                    .hasZeminEtudu(true)
                    .areaSize(1500.0)
                    .locationDetails("Antalya / Muratpaşa")
                    .build());

            // ---- ÖRNEK TEKLİFLER OLUŞTUR ----
            Offer offer1 = offerRepository.save(Offer.builder()
                    .title("Kartal Projesi İçin Teklifim")
                    .description("Belirtilen işi 6 ay içinde tamamlayabilirim.")
                    .proposedPrice(450000.0)
                    .status("PENDING")
                    .sender(planner1)
                    .job(job1)
                    .build());

            Offer offer2 = offerRepository.save(Offer.builder()
                    .title("Çankaya Yaya Bölgesi Planlaması")
                    .description("Daha önce benzer yaya yolu planlamaları yaptım, bu bütçeye uygun olarak yapabiliriz.")
                    .proposedPrice(180000.0)
                    .status("NEGOTIATING")
                    .sender(planner2)
                    .job(job2)
                    .build());

            Offer offer3 = offerRepository.save(Offer.builder()
                    .title("Yeşil Altyapı Planı - Teklif")
                    .description("Tüm süreçleri C ve D grubu partnerlerimle beraber yöneteceğiz.")
                    .partnerKarnes("C, D")
                    .proposedPrice(320000.0)
                    .status("ACCEPTED")
                    .sender(planner2)
                    .job(job3)
                    .build());

            Offer offer4 = offerRepository.save(Offer.builder()
                    .title("Ulaşım Entegrasyon Danışmanlığı")
                    .description("İlgili bölgede tüm ulaşım simülasyonlarını sağlayabilirim.")
                    .proposedPrice(280000.0)
                    .status("ACCEPTED")
                    .sender(planner1)
                    .job(job4)
                    .build());

            // ---- ÖRNEK YORUMLAR OLUŞTUR ----
            commentRepository.save(Comment.builder()
                    .offer(offer1)
                    .user(entity1)
                    .text("Merhaba Ahmet Bey, proje kapsamı hakkında detaylı bilgi almak için bir toplantı ayarlayabilir miyiz?")
                    .createdAt(LocalDateTime.now().minusDays(3))
                    .build());

            commentRepository.save(Comment.builder()
                    .offer(offer1)
                    .user(planner1)
                    .text("Merhaba! Tabii ki, haftaya Salı veya Çarşamba günü uygun olur. Teklifin detaylarını inceledim, gerçekçi bir süre ve bütçe çerçevesi gibi görünüyor.")
                    .createdAt(LocalDateTime.now().minusDays(2))
                    .build());

            commentRepository.save(Comment.builder()
                    .offer(offer2)
                    .user(planner2)
                    .text("Teklifi inceledim. Mevcut bütçe biraz düşük kalabilir. 220.000 TL üzerinden tekrar değerlendirebilir misiniz?")
                    .createdAt(LocalDateTime.now().minusDays(1))
                    .build());

            commentRepository.save(Comment.builder()
                    .offer(offer2)
                    .user(entity2)
                    .text("Anladım, değerlendireceğiz. Projenin kapsamı ve teslimat takvimini netleştirirsek bütçeyi gözden geçirebiliriz.")
                    .createdAt(LocalDateTime.now().minusHours(5))
                    .build());

            commentRepository.save(Comment.builder()
                    .offer(offer3)
                    .user(entity1)
                    .text("Teklif kabul edildi. Projeye en kısa sürede başlamamızı bekliyoruz. Sözleşme taslağını hazırlıyoruz.")
                    .createdAt(LocalDateTime.now().minusHours(2))
                    .build());

            // ---- ÖRNEK PARTNERLİK TEKLİFİ OLUŞTUR ----
            partnerOfferRepository.save(PartnerOffer.builder()
                    .sender(planner1)
                    .receiver(planner2)
                    .job(job1)
                    .message("Merhaba Ayşe Hanım, Kartal İmar Planı projesi için B grubu karnenize ve yeşil altyapı deneyiminize ihtiyacım var. Birlikte çalışabilir miyiz?")
                    .proposedFee(50000.0)
                    .status("PENDING")
                    .build());
        }
    }
}
