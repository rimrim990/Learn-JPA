package jpabasic.jpashop;

import javax.persistence.MappedSuperclass;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;

// 속성만 상속. 엔티티 X
@MappedSuperclass
// 직접 생성하지 않으므로 추상 클래스로 설정
public abstract class BaseEntity {

    // timestamp
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;

    // date
    private LocalDate date;

    // time
    private LocalTime time;

    public LocalDateTime getCreatedAt() {
        return createdAt;
    }

    public void setCreatedAt(LocalDateTime createdAt) {
        this.createdAt = createdAt;
    }

    public LocalDateTime getUpdatedAt() {
        return updatedAt;
    }

    public void setUpdatedAt(LocalDateTime updatedAt) {
        this.updatedAt = updatedAt;
    }

    public LocalDate getDate() {
        return date;
    }

    public void setDate(LocalDate date) {
        this.date = date;
    }

    public LocalTime getTime() {
        return time;
    }

    public void setTime(LocalTime time) {
        this.time = time;
    }
}
