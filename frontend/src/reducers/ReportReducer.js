const initialState = {
  formData: {
    minCount: 0,
    maxCount: 0,
    email: "",
    isOutOfStock: false,
    isSentToEmail: false,
  },
};

const reportFormReducer = (state = initialState, action) => {
  return state;
};

export default reportFormReducer;
