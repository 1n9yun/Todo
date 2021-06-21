<template>
    <v-date-picker
    v-model="watchedDates"
    locale="ko-KR"
    multiple
    full-width
    scrollable
    color="light-blue lighten-1"
    @click:date="multiSelect"
    ></v-date-picker>
</template>

<script>
import {mapActions} from 'vuex';

export default {
    name: "MultiSelectableDatePicker",
    data() {
        return {
            watchedDates: this.$props.dates,
            prevClickedDate: null
        }
    },
    props: [
        "dates",
    ],
    watch: {
        watchedDates: function(val) {
            this.setDates(val);
        }
    },
    methods: {
        ...mapActions("calendar", ["setDates"]),
        multiSelect: function(val, event) {
            if(event.shiftKey) {
                var betweenDates = this.getDatesBetween(new Date(this.prevClickedDate), new Date(val));

                this.watchedDates = [...new Set(this.watchedDates.concat(betweenDates))];
            }
            this.prevClickedDate = val;
        },
        getDatesBetween(leftDate, rightDate) {
            if(leftDate > rightDate) {
                var temp = leftDate;
                leftDate = rightDate;
                rightDate = temp;
            }
            var dateArray = [];

            var currentDate = this.plusDays(leftDate, 1);

            while(currentDate < rightDate) {
                dateArray.push(currentDate.toISOString().substring(0, 10));
                currentDate = this.plusDays(currentDate, 1);
            }
            return dateArray;
        },
        plusDays(date, days) {
            var nextDate = new Date(date.valueOf());

            nextDate.setDate(nextDate.getDate() + days);
            return nextDate;
        }
    },

};
</script>

<style>
</style>