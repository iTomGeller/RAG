import { ref } from 'vue';
import run from '../config/gemini'; 

export function useGemini() {
  const input = ref('');
  const recentPrompt = ref('');
  const prevPrompts = ref([]);
  const showResult = ref(false);
  const loading = ref(false);
  const resultData = ref('');

  const delayPara = (index, nextWord) => {
    setTimeout(function() {
      resultData.value += nextWord;
    }, 75 * index);
  };

  const newChat = () => {
    loading.value = false;
    showResult.value = false;
    input.value = '';
    resultData.value = '';
  };

  const onSent = async (prompt) => {
    resultData.value = '';
    loading.value = true;
    showResult.value = true;

    let response;

    if (prompt !== undefined) {
      response = await run(prompt);
      recentPrompt.value = prompt;
      prevPrompts.value.push(prompt);
    } else {
      prevPrompts.value.push(input.value);
      recentPrompt.value = input.value;
      response = await run(input.value);
    }

    let responseArray = response.split("**");
    let newResponse = '';
    for (let i = 0; i < responseArray.length; i++) {
      if (i % 2 === 0) { 
        newResponse += responseArray[i];
      } else {
        newResponse += " <b>" + responseArray[i] + "</b>";
      }
    }
    let newResponse2 = newResponse.split("*").join("</br>");
    let newResponseArray = newResponse2.split(" ");
    for (let i = 0; i < newResponseArray.length; i++) {
      const nextWord = newResponseArray[i];
      delayPara(i, nextWord + " ");
    }

    loading.value = false;
    input.value = '';
  };

  return {
    input,
    setInput: (val) => { input.value = val; }, // Setter for input ref
    recentPrompt,
    setRecentPrompt: (val) => { recentPrompt.value = val; }, // Setter for recentPrompt ref
    prevPrompts,
    setPrevPrompts: (arr) => { prevPrompts.value = arr; }, // Setter for prevPrompts ref
    showResult,
    setShowResult: (val) => { showResult.value = val; }, // Setter for showResult ref
    loading,
    setLoading: (val) => { loading.value = val; }, // Setter for loading ref
    resultData,
    setResultData: (val) => { resultData.value = val; }, // Setter for resultData ref
    onSent,
    newChat,
  };
}