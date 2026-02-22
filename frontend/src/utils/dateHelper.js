import { format, parseISO } from 'date-fns';
import { ru } from 'date-fns/locale';

// Форматирование для отображения в UI (например: 25 октября 2025 14:30)
export const formatDisplayDate = (dateString) => {
  if (!dateString) return '';
  return format(new Date(dateString), 'dd MMMM yyyy HH:mm', { locale: ru });
};

// Группировка слотов (оставляем как было, логика верная)
export const groupSlotsToRanges = (slots) => {
  if (!slots || slots.length === 0) return [];
  const sorted = [...slots].sort((a, b) => new Date(a.startTime) - new Date(b.startTime));
  const ranges = [];
  let currentRange = null;

  sorted.forEach((slot) => {
    const slotStart = new Date(slot.startTime).getTime();
    const slotEnd = new Date(slot.endTime).getTime();

    if (!currentRange) {
      currentRange = { 
        ...slot, 
        startTime: slot.startTime, 
        endTime: slot.endTime,
        originalSlots: [slot.id] 
      };
    } else {
      const prevEnd = new Date(currentRange.endTime).getTime();
      if (slotStart === prevEnd && slot.room === currentRange.room && slot.district === currentRange.district) {
        currentRange.endTime = slot.endTime;
        currentRange.originalSlots.push(slot.id);
      } else {
        ranges.push(currentRange);
        currentRange = { 
          ...slot, 
          startTime: slot.startTime, 
          endTime: slot.endTime,
          originalSlots: [slot.id]
        };
      }
    }
  });
  if (currentRange) ranges.push(currentRange);
  return ranges;
};

// --- ИСПРАВЛЕНО ЗДЕСЬ ---
// Преобразует дату в строку "yyyy-MM-ddTHH:mm" без секунд и Z
export const toServerDate = (dateInput) => {
  if (!dateInput) return null;
  const date = new Date(dateInput);
  // Форматируем дату в строку "2023-12-31T15:30"
  // Это сохраняет то время, которое выбрал пользователь (local time),
  // и убирает секунды и таймзону.
  return format(date, "yyyy-MM-dd'T'HH:mm");
};