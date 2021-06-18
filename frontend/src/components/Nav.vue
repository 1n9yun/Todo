<template>
    <v-layout 
    column
    >
        <Profile
        :name="$props.name"
        :email="$props.email"
        />
        <div class="btns">
            <v-btn-toggle
            borderless
            tile
            >
                <v-btn
                class="justify-start"
                >
                    <v-icon
                    left
                    color="yellow"
                    >
                        mdi-white-balance-sunny
                    </v-icon>
                    오늘 할 일
                </v-btn>
                <v-btn
                class="justify-start"
                >Test Button 2dddddddddddddddddddddd</v-btn>
                <v-btn
                class="justify-start"
                >Test Button 3</v-btn>
                <v-btn
                class="justify-start"
                >Test Button 4</v-btn>
            </v-btn-toggle>
        </div>
    </v-layout>
</template>

<script>
import Profile from '@/components/Profile';

export default {
    name: "Nav",
    components: {
        Profile,
    },
    props: [
        "name",
        "email",
    ],
};
</script>
<style scoped>
.layout {
    background-color: var(--v-navBackground-base);
}
.v-btn-toggle {
    flex-direction: column;
    width: 100%;
}
.v-btn {
    overflow: hidden;
    padding-left: 24px !important;
}
.theme--dark.v-btn.v-btn--has-bg, .theme--light.v-btn.v-btn--has-bg {
    background-color: var(--v-background-base) !important;
}
</style>