<template>
    <v-layout column justify-center>
        <v-flex align-self-center style="width: 300px;">
            <h1>안녕하세요!</h1><br>
            <v-text-field
            placeholder="이메일"
            outlined
            dense
            ></v-text-field>
            <v-text-field
            :type="'password'"
            placeholder="패스워드"
            outlined
            dense
            ></v-text-field>
            <v-btn
            style="width: 100%;"
            >로그인</v-btn>
        </v-flex>
    </v-layout>
</template>

<script>
export default {

}
</script>

<style>
</style>