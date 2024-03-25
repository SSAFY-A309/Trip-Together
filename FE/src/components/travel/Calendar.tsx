import React from 'react';
import {Calendar} from 'react-native-calendars';
import {StyleSheet} from 'react-native';
import {Wrapper, DateContainer, DateText} from './CalendarStyle';

interface CalendarViewProps {
  startDate: string;
  endDate: string;
  setStartDate: React.Dispatch<React.SetStateAction<string>>;
  setEndDate: React.Dispatch<React.SetStateAction<string>>;
}

const CalendarView = ({
  startDate,
  endDate,
  setStartDate,
  setEndDate,
}: CalendarViewProps) => {
  const getDatesBetweenDates = (start: string, end: string) => {
    const dates: any = {};
    const currentDate = new Date(start);
    while (currentDate <= new Date(end)) {
      const dateString = currentDate.toISOString().split('T')[0];
      dates[dateString] = {selected: true, color: 'green', textColor: 'white'};
      currentDate.setDate(currentDate.getDate() + 1);
    }
    return dates;
  };

  const markedDates = {
    ...getDatesBetweenDates(startDate, endDate || startDate),
    [startDate]: {selected: true},
    [endDate]: {selected: true},
  };

  const handleDayPress = (day: any) => {
    const selectedDate = day.dateString;
    const today = new Date().toISOString().split('T')[0];

    if (selectedDate < today) {
      return;
    }

    if (!startDate) {
      setStartDate(selectedDate);
    } else if (!endDate && selectedDate > startDate) {
      setEndDate(selectedDate);
    } else {
      setStartDate(selectedDate);
      setEndDate('');
    }
  };

  return (
    <Wrapper>
      <Calendar
        style={styles.calendar}
        markedDates={markedDates}
        theme={{
          selectedDayBackgroundColor: 'green',
          arrowColor: 'blue',
          dotColor: 'green',
          todayTextColor: 'red',
        }}
        onDayPress={handleDayPress}
      />
      <DateContainer>
        <DateText>시작 일자</DateText>
        <DateText>{startDate}</DateText>
      </DateContainer>
      <DateContainer>
        <DateText>종료 일자</DateText>
        <DateText>{endDate}</DateText>
      </DateContainer>
    </Wrapper>
  );
};

const styles = StyleSheet.create({
  calendar: {
    borderBottomWidth: 1,
    borderBottomColor: '#e0e0e0',
    marginBottom: 100,
    width: 400,
  },
});

export default CalendarView;
