<template>
    <v-row>
        <v-col
        cols="12"
        >
            <v-btn-toggle
            :value="days"
            @change="$emit('update:days', $event)"
            multiple
            rounded
            style="width:100%;"
            >
                <v-btn
                v-for="day in ['일','월','화','수','목','금','토']"
                :key="day"
                :width="100 / 7 + '%'"
                >
                    {{day}}
                </v-btn>
            </v-btn-toggle>
        </v-col>
    </v-row>
</template>

<script>
export default {
    props: [
        'days',
    ],
};
</script>

<style>
</style>