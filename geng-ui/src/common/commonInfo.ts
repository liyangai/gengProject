import { ref } from "vue";

const isCollapse = ref(false);

export const commonInfo = {
  isCollapse,
  setIsCollapse: function (val: boolean) {
    isCollapse.value = val;
  },
};
