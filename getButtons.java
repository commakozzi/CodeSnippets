//Define calculator button ViewGroup
mCalcButtonViewGroup = (ViewGroup) findViewById(R.id.calcScreen_fragment);

//Get buttons from calcScreen_fragment:
getButtons(mCalcButtonViewGroup);

//Method for getting buttons:
private void getButtons(ViewGroup v) {
    for (int i = 0; i < v.getChildCount(); i++) {
        View child = v.getChildAt(i);
        if (child instanceof ViewGroup) {
            getButtons((ViewGroup) child);
        } else if (child instanceof Button) {
            mButtons.add((Button) child);
        }
    }
}
