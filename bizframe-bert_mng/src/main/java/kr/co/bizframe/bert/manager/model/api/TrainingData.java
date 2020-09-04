package kr.co.bizframe.bert.manager.model.api;

import java.util.List;

public class TrainingData {

	private String context;
	private List<QuestionSet> qas;

	public String getContext() {
		return context;
	}

	public void setContext(String context) {
		this.context = context;
	}

	public List<QuestionSet> getQuestions() {
		return qas;
	}

	public void setQuestions(List<QuestionSet> questions) {
		this.qas = questions;
	}

	public static class QuestionSet {
		private String question;
		private List<Answer> answers;

		public String getQuestion() {
			return question;
		}

		public void setQuestion(String question) {
			this.question = question;
		}

		public List<Answer> getAnswers() {
			return answers;
		}

		public void setAnswers(List<Answer> answers) {
			this.answers = answers;
		}

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("QuestionSet [question=");
			builder.append(question);
			builder.append(", answers=");
			builder.append(answers);
			builder.append("]");
			return builder.toString();
		}

	}

	public static class Answer {
		private String text;
		private int answer_start;
		private int answer_end;

		public String getText() {
			return text;
		}

		public void setText(String text) {
			this.text = text;
		}

		public int getAnswer_start() {
			return answer_start;
		}

		public void setAnswer_start(int answer_start) {
			this.answer_start = answer_start;
		}

		public int getAnswer_end() {
			return answer_end;
		}

		public void setAnswer_end(int answer_end) {
			this.answer_end = answer_end;
		}

		@Override
		public String toString() {
			StringBuilder builder = new StringBuilder();
			builder.append("Answer [text=");
			builder.append(text);
			builder.append(", answer_start=");
			builder.append(answer_start);
			builder.append(", answer_end=");
			builder.append(answer_end);
			builder.append("]");
			return builder.toString();
		}

	}

	@Override
	public String toString() {
		StringBuilder builder = new StringBuilder();
		builder.append("TrainingData [context=");
		builder.append(context);
		builder.append(", questions=");
		builder.append(qas);
		builder.append("]");
		return builder.toString();
	}

}
