// 보통 js 부분을 분리해서 화면영역과 통신하는 영역을 분리하게 만든다. 이런 방식이 좋을 수 있다.
// goLast는 댓글의 마지막 부분으로 가게 만들기 위한 것이다. 나머지는 게시물 번호, 페이징 데이터들이다.
async function getList({bno, page, size, goLast}){

    const result = await axios.get(`/replies/list${bno}`, {params: {page, size}});

    if(goLast){
        const total = result.data.total;
        const lastPage = parseInt(Math.ceil(total/size));
        return getList({bno:bno, page:lastPage, size:size});
    }

    return result.data;
}

async function get1(bno){
    const result = await axios.get(`/replies/list${bno}`);
    return result;
}

async function addReply(replyObj) {
    const response = await axios.post(`/replies/`,replyObj)
    return response.data
}

async function getReply(rno) {
    const response = await axios.get(`/replies/${rno}`)
    return response.data
}

async function modifyReply(replyObj) {

    const response = await axios.put(`/replies/${replyObj.rno}`, replyObj)
    return response.data
}

async function removeReply(rno) {
    const response = await axios.delete(`/replies/${rno}`)
    return response.data
}