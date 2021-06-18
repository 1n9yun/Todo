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
                    color="var(--v-primary-base)"
                    >
                        mdi-pencil
                    </v-icon>
                    Test
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
}
.theme--dark.v-btn.v-btn--has-bg, .theme--light.v-btn.v-btn--has-bg {
    background-color: var(--v-background-base) !important;
}
</style>