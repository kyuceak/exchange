package com.kutay.exchange.shared.model;

import jakarta.persistence.Column;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.PrePersist;
import jakarta.persistence.PreUpdate;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.annotation.CreatedDate;
import org.springframework.data.annotation.LastModifiedDate;

import java.io.Serial;
import java.io.Serializable;
import java.time.Instant;

@Getter
@Setter
@MappedSuperclass // Tell JPA/Hibernate that this ia base class for future entities
public abstract class AbstractBaseEntity implements Serializable {
    /*
     * Neden serialVersionUID Kullanıyoruz?
     * * 1. Java Serialization Mekanizması: Java, bir nesneyi byte dizisine çevirirken (serialize),
     * sınıfın yapısına (fieldlarına) bakarak otomatik ve benzersiz bir hash (UID) üretir.
     * * 2. Problem (Versiyon Uyuşmazlığı): İleride bu Entity'e yeni bir field eklersek,
     * Java'nın hesapladığı otomatik UID değişir.
     * Eğer Redis'te eski versiyon (eski UID) saklıysa ve biz kodu güncelleyip (yeni UID)
     * okumaya çalışırsak "InvalidClassException" hatası alırız.
     * * 3. Çözüm: serialVersionUID'yi manuel ve sabit (örn: 1L) tanımlayarak, Java'ya şunu söyleriz:
     * "Sınıfa yeni field eklesem bile bu hala Versiyon 1'dir. Hata fırlatma,
     * eski veriyi yükle, yeni eklenen fieldları boş (null) bırak."
     * */
    @Serial // serialization compile time check
    private static final long serialVersionUID = 1L; // force ID to be same during serialization and deserialization

    @CreatedDate // not active without auditing listener, its for documentatiton purposes
    @Column(name = "created_at", nullable = false, updatable = false)
    private Instant createdAt;

    @LastModifiedDate
    @Column()
    private Instant updatedAt;

    @PrePersist
    public void prePersist() {
        Instant now = Instant.now();
        if (createdAt == null) {
            createdAt = now;
        }
        updatedAt = now;
    }

    @PreUpdate
    protected void onUpdate() {
        this.updatedAt = Instant.now();
    }
}
